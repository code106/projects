const snowflake = require('snowflake-sdk');

// Create a connection object with SSO authentication
const connection = snowflake.createConnection({
  account: 'your_account_name',  // Example: 'xyz123.us-east-1'
  authenticator: 'EXTERNALBROWSER', // Enables SSO authentication
  warehouse: 'your_warehouse',
  database: 'your_database',
  schema: 'your_schema'
});

// Connect to Snowflake using SSO
connection.connect((err, conn) => {
  if (err) {
    console.error('Connection failed:', err.message);
    return;
  }
  console.log('Successfully connected to Snowflake. Connection ID:', conn.getId());

  // Example Query Execution
  connection.execute({
    sqlText: 'SELECT CURRENT_USER()', 
    complete: (err, stmt, rows) => {
      if (err) {
        console.error('Error executing query:', err.message);
      } else {
        console.log('Query Result:', rows);
      }

      // Close the connection
      connection.destroy();
    }
  });
});
