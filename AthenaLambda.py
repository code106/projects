import boto3
import os
import time

# Athena Client
athena_client = boto3.client('athena', region_name=os.environ['AWS_REGION'])

def execute_ddl(query):
    """Execute a single Athena query and wait for completion."""
    response = athena_client.start_query_execution(
        QueryString=query,
        QueryExecutionContext={'Database': os.environ['ATHENA_DATABASE']},
        ResultConfiguration={'OutputLocation': os.environ['ATHENA_OUTPUT_S3']}
    )
    
    query_execution_id = response['QueryExecutionId']
    print(f"Executing Query ID: {query_execution_id}")

    # Wait for query to complete
    while True:
        query_status = athena_client.get_query_execution(QueryExecutionId=query_execution_id)
        status = query_status['QueryExecution']['Status']['State']
        
        if status in ['SUCCEEDED', 'FAILED', 'CANCELLED']:
            break
        time.sleep(2)  # Wait before checking again

    if status == 'SUCCEEDED':
        print(f"Query succeeded: {query}")
    else:
        print(f"Query failed: {query}")

    return status

def lambda_handler(event, context):
    try:
        # List of DDL statements
        ddl_statements = [
            "CREATE TABLE IF NOT EXISTS employees (id INT, name STRING, salary FLOAT) LOCATION 's3://your-bucket/athena/employees/'",
            "ALTER TABLE employees ADD COLUMNS (department STRING)",
            "CREATE TABLE IF NOT EXISTS departments (dept_id INT, dept_name STRING) LOCATION 's3://your-bucket/athena/departments/'"
        ]

        # Execute each DDL statement
        for ddl in ddl_statements:
            status = execute_ddl(ddl)
            if status != 'SUCCEEDED':
                return {"status": "Failure", "message": f"Query failed: {ddl}"}

        return {"status": "Success", "message": "All DDL statements executed successfully"}

    except Exception as e:
        print(f"Error: {str(e)}")
        return {"status": "Failure", "message": str(e)}
