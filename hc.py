import hvac

# Initialize the Vault client
client = hvac.Client(url='https://your-vault-url.com', token='your-vault-token')

# Get the client token using the AppRole auth method
role_id = 'your-role-id'
secret_id = 'your-secret-id'

response = client.auth_approle(role_id, secret_id)
if response and response.get('auth') and response['auth'].get('client_token'):
    client.token = response['auth']['client_token']

# Now you can read a secret
secret_path = 'secret/myapp/config'
secret_data = client.read(secret_path)

if secret_data and secret_data.get('data'):
    actual_secret = secret_data['data']
    print(f"Secret data: {actual_secret}")
else:
    print("Failed to retrieve secret.")
