#prerequisite pip install requests

import requests
import json

# SharePoint Online URL
sharepoint_url = 'https://your_sharepoint_site_url'

# Authentication details
client_id = 'your_client_id'
client_secret = 'your_client_secret'
tenant_id = 'your_tenant_id'

# Get access token using client credentials flow
token_url = f'https://login.microsoftonline.com/{tenant_id}/oauth2/v2.0/token'
token_data = {
    'grant_type': 'client_credentials',
    'client_id': client_id,
    'client_secret': client_secret,
    'scope': 'https://graph.microsoft.com/.default'
}
token_r = requests.post(token_url, data=token_data)
access_token = json.loads(token_r.text)['access_token']

# File to upload
file_path = 'path_to_your_file_to_upload'

# SharePoint endpoint to upload file
upload_url = f"{sharepoint_url}/_api/web/GetFolderByServerRelativeUrl('/sites/yoursite/Shared Documents')/Files/add(url='{file_path}',overwrite=true)"

# Read file content
with open(file_path, 'rb') as file:
    file_content = file.read()

# Upload file to SharePoint
headers = {
    'Authorization': f'Bearer {access_token}',
    'Accept': 'application/json;odata=verbose',
}
upload_response = requests.post(upload_url, headers=headers, data=file_content)

if upload_response.status_code == 200:
    print('File uploaded successfully!')
else:
    print('Failed to upload file. Status code:', upload_response.status_code)
    print(upload_response.text)
