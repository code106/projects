import requests
from requests_ntlm import HttpNtlmAuth
import boto3
import os

# SharePoint details
SHAREPOINT_SITE = "https://your-sharepoint-site.sharepoint.com/sites/YourSite"
SHAREPOINT_FOLDER = "/Shared Documents/YourFolder"  # Path to the folder in SharePoint
USERNAME = "your-email@domain.com"
PASSWORD = "your-password"

# AWS S3 details
AWS_ACCESS_KEY = "your-aws-access-key"
AWS_SECRET_KEY = "your-aws-secret-key"
S3_BUCKET = "your-s3-bucket-name"
S3_FOLDER = "sharepoint-data/"  # Folder in the S3 bucket

# Initialize S3 client
s3_client = boto3.client(
    "s3",
    aws_access_key_id=AWS_ACCESS_KEY,
    aws_secret_access_key=AWS_SECRET_KEY,
)

def list_sharepoint_files():
    """Fetch the list of files in the SharePoint folder."""
    url = f"{SHAREPOINT_SITE}/_api/web/getfolderbyserverrelativeurl('{SHAREPOINT_FOLDER}')/files"
    response = requests.get(url, auth=HttpNtlmAuth(USERNAME, PASSWORD), headers={"Accept": "application/json"})
    response.raise_for_status()
    return response.json()["d"]["results"]

def download_file(file_url, file_name):
    """Download a file from SharePoint."""
    response = requests.get(file_url, auth=HttpNtlmAuth(USERNAME, PASSWORD))
    response.raise_for_status()
    with open(file_name, "wb") as f:
        f.write(response.content)
    print(f"Downloaded {file_name}")

def upload_to_s3(file_name, s3_key):
    """Upload a file to S3."""
    s3_client.upload_file(file_name, S3_BUCKET, s3_key)
    print(f"Uploaded {file_name} to s3://{S3_BUCKET}/{s3_key}")

def main():
    files = list_sharepoint_files()
    for file in files:
        file_url = file["ServerRelativeUrl"]
        file_name = file["Name"]
        local_file_path = os.path.join("/tmp", file_name)

        # Download file from SharePoint
        download_file(f"{SHAREPOINT_SITE}{file_url}", local_file_path)

        # Upload file to S3
        upload_to_s3(local_file_path, f"{S3_FOLDER}{file_name}")

        # Remove local file
        os.remove(local_file_path)

if __name__ == "__main__":
    main()
