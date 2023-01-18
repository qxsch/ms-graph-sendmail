from config import config
from kiota_authentication_azure.azure_identity_authentication_provider import (AzureIdentityAuthenticationProvider)
from msgraph import GraphRequestAdapter, GraphServiceClient
from msgraph.generated.me.send_mail.send_mail_post_request_body import SendMailPostRequestBody
from msgraph.generated.models.message import Message
from msgraph.generated.models.item_body import ItemBody
from msgraph.generated.models.body_type import BodyType
from msgraph.generated.models.recipient import Recipient
from msgraph.generated.models.email_address import EmailAddress
from azure.identity import ClientSecretCredential



client_credential = ClientSecretCredential(config["tenantid"], config["clientid"], config["clientsecret"])
auth_provider = AzureIdentityAuthenticationProvider(client_credential, scopes="Mail.Send")

adapter = GraphRequestAdapter(auth_provider)
user_client = GraphServiceClient(adapter)

message = Message()
message.subject = "testmail"

message.body = ItemBody()
message.body.content_type = BodyType.Text
message.body.content = "this is a test mail from azure graph"

to_recipient = Recipient()
to_recipient.email_address = EmailAddress()
to_recipient.email_address.address = config["sendmailTo"]
message.to_recipients = []
message.to_recipients.append(to_recipient)

request_body = SendMailPostRequestBody()
request_body.message = message

result = user_client.users_by_id(config["mailboxuserid"]).send_mail().post(body=request_body)

print(result)

