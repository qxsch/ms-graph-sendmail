package ch.marcoweber;

import java.util.*;
import java.util.logging.Logger;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.UserSendMailParameterSet;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.requests.GraphServiceClient;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;


public class DemoApplication {

    private static final Logger log;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n");
        log = Logger.getLogger(DemoApplication.class.getName());
    }


    public static void sendMailAppId(String subject, String plainText) throws Exception {
        Properties properties = new Properties();
        properties.load(DemoApplication.class.getClassLoader().getResourceAsStream("application.properties"));

        log.info("Creating GraphServiceClient for clientid " + properties.getProperty("clientid") + " and tenantid " + properties.getProperty("tenantid"));

        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
            .clientId(properties.getProperty("clientid"))
            .clientSecret(properties.getProperty("clientsecret"))
            .tenantId(properties.getProperty("tenantid"))
            .build();

            
        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(List.of("https://graph.microsoft.com/.default"), clientSecretCredential);

        final GraphServiceClient graphClient = GraphServiceClient
            .builder()
            .authenticationProvider(tokenCredentialAuthProvider)
            .buildClient();

        log.info("Sending mail from " + properties.getProperty("mailboxuserid") + " to " + properties.getProperty("mailboxuserid") + " with subject " + subject);


        Message message = new Message();
        message.subject = subject;
        ItemBody body = new ItemBody();
        body.contentType = BodyType.TEXT;
        body.content = plainText;
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = properties.getProperty("sendmailTo");
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        
        boolean saveToSentItems = false;
        
        graphClient.getLogger().setLoggingLevel(LoggerLevel.DEBUG);
        graphClient.users(properties.getProperty("mailboxuserid"))
            .sendMail(UserSendMailParameterSet
                .newBuilder()
                .withMessage(message)
                .withSaveToSentItems(saveToSentItems)
                .build())
            .buildRequest()
            .post();
    }

    public static void main(String[] args) throws Exception {
        sendMailAppId("testmail", "this is a test mail from azure graph");
    }
}
