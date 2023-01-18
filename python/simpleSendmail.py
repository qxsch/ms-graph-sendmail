from config import config
import requests
import urllib.parse as urlparse

fromMailbox = config["mailboxuserid"]
tenantId = config["tenantid"]

formData = {
    "client_id" : config["clientid"],
    "client_secret" : config["clientsecret"],
    "scope" : "https://graph.microsoft.com/.default",
    "grant_type" : "client_credentials"
}
r = requests.post(
    "https://login.microsoftonline.com/" + urlparse.quote(tenantId) + "/oauth2/v2.0/token",
    data = formData
)
j = r.json()

if "access_token" in j:
    print("Got a token - Sending mail")
    r = requests.post(
        "https://graph.microsoft.com/v1.0/users/" + urlparse.quote(fromMailbox) + "/graph.sendMail",
        headers={
            "Authorization" : ("Bearer " + j["access_token"])
        },
        json={
            "message": {
                "subject": "testmail",
                "body": {
                    "contentType": "Text",
                    "content": "this is a test mail from azure graph"
                },
                "toRecipients": [
                    {
                        "emailAddress": {
                            "address": config["sendmailTo"]
                        }
                    }
                ]
            },
            "saveToSentItems": "false"
        }
    )
    print("Status is: " + str(r.status_code))
    print("Response is: " + str(r.text))

else:
    print("Couldn't get a token")
