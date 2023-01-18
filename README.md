# Microsoft Graph Send Mail
Send mail as an application for a user mailbox

## Setup

## Setting up the application
 1. Open [Azure Active Directory in the Azure Portal](https://portal.azure.com/#view/Microsoft_AAD_IAM/ActiveDirectoryMenuBlade/~/Overview)
 1. Click on ``App Registrations`` 
 1. Add a new App Registration by clicking on ``New Registration``
    1. Give it a name (f.e. graphsendmail)
    1. Select ``Accounts in this organizational directory only (Single tenant)``
    1. Click on register
 1. You should now see your App Registration (if not please go to app registrations and then select it by name)
 1. ?????? Allow public client flows ??????
 1. <span style="color:red">Memorize the Application (client) ID</span>
 1. Go to ``Certificates & secrets`` and add a ``New client secret``
 1. <span style="color:red">Memorize the client secret</span>
 1. Go to ``API permissions`` and click on ``Add a permission``
    1. Click on ``Microsoft Graph``
    1. Click on ``Application permissions``
    1. Select ``Mail.Send``
    1. Click on ``Add permissions``
 1. Click on ``Grant Admin Consent``<br>(This will give the application access to send mails on behalf of all users, that we will limit later to specific mailboxes.)
 1. Configure an applcation (choose either java or python)
    * For Java add client id, client secret and a mailbox upn to the [application.properties](java/src/main/resources/application.properties) file
    * For Python add to the python [config.py](python/config.py) file

## Limiting access to specific mailboxes (optional)
 1. Go to [Exchange Admin Center](https://admin.exchange.microsoft.com/)
 1. Click on ``Groups`` (Under ``Recipients``)
 1. Click on ``Add a group``
    1. Select ``Mail-enabled security`` and click on ``Next``
    1. Give it a name (f.e. graphsendmailgroup) and click on ``Next``
    1. Add an owner
    1. Add here the mailboxes as members, that you would like to use with your applicaton.
    1. Use the name as group mail address and <span style="color:red">memorize the mail address of the group</span>
    1. Click on ``Create``
 1. Run the following script to create an application policy
    ```pwsh
    # connect to exchange online
    Connect-ExchangeOnline
    # add a new application policy (take the memorized data)
    New-ApplicationAccessPolicy -AppId ClientAppId -PolicyScopeGroupId mailEnabledSecurityGroupName@domain -AccessRight RestrictAccess -Description "Restrict this app to members of distribution group EvenUsers."
    # test if the application can use a mailbox (that is member of the group)
    Test-ApplicationAccessPolicy -Identity mailbox@domain -AppId ClientAppId
    ```

## Run Demo
  1. Go into the directory of the application (choose either java or python)
     * For Java go to [java directory](java/README.md)
     * For Python go to [python directory](python/README.md)

