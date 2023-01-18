# Run the python demo

## Setup
Make sure, that:
 * you did follow the [setup instructions](../README.md)
 * you updated the [config.py](config.py) file
 * you installed the required packages: ``pip install msgraph-sdk azure-identity requests``

# Run the Demo
Run the following command in this folder:
```bash
# send a mail using graph sdk
python sendmail.py
# send a mail using requests
python simpleSendmail.py
```