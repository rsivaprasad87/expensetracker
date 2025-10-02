# Expense Tracker - Spring Boot + AWS Lambda + DynamoDB

A sample project demonstrating a **Spring Boot application deployed on AWS Lambda**, exposing REST APIs via **API Gateway**, and persisting data in **DynamoDB**. Supports **local development with DynamoDB Local** and full **CRUD operations**.

---

## Features

- CRUD operations for `Expense` entity
- Runs locally using **DynamoDB Local**
- Deploys to **AWS Lambda** with **API Gateway**
- Handles CORS for Postman and browser testing
- Configurable Spring Profiles: `local` / `aws`

---

## Architecture

```
[Postman / Browser] <--HTTP--> [API Gateway] <--Invoke--> [AWS Lambda: Spring Boot] <--AWS SDK--> [DynamoDB]
```
Local mode:
```
[Spring Boot App] <--HTTP--> [DynamoDB Local]
```

---

## Requirements

- Java 17
- Maven 3.8+
- AWS CLI
- SAM CLI
- Docker (for SAM local testing)
- DynamoDB Local (Docker or JAR)

---

## Setup

1. Clone repository:
```bash
git clone https://github.com/rsivaprasad87/expensetracker.git
cd expensetracker
```

2. Build project:
```bash
mvn clean package
```

3. Ensure AWS CLI is configured:
```bash
aws configure
```

---

## Running Locally

### 1. Start DynamoDB Local

**Docker:**
```bash
docker run -p 8000:8000 amazon/dynamodb-local  -> This creates a container in local
```
### 2. Create `Expenses` Table (if not exists)
```bash
aws dynamodb create-table --table-name Expenses --attribute-definitions AttributeName=expenseId,AttributeType=S --key-schema AttributeName=expenseId,KeyType=HASH --billing-mode PAY_PER_REQUEST --region ap-south-1 --endpoint-url http://host.docker.internal:8000
aws dynamodb list-tables --endpoint-url http://host.docker.internal:8000 --region ap-south-1 --output json
aws dynamodb describe-table --table-name Expenses --endpoint-url http://host.docker.internal:8000 --output json	  

```

### 3. Set Spring Profile and Run
```bash
To run the spring boot in local use below as VM option
-Dspring.profiles.active=local

Set environment variable in Lambda to use local profile:

Environment:
  Variables:
    SPRING_PROFILES_ACTIVE: local
```


### 4. Test API in local
```bash
mvn clean build
sam build
sam local start-api --debug --> This will create a container for lambda in docker locally

Endpoints:
http://localhost:8080/swagger-ui.html
http://127.0.0.1:3000/expenses/addExpense  --> POST method
http://127.0.0.1:3000/expenses/getAllExpenses --> Get endpoint

```
---

## Deploying to AWS

1.Set environment variable in Lambda to use AWS profile:
```yaml
Environment:
  Variables:
    SPRING_PROFILES_ACTIVE: aws
```

2.Build with SAM:
```bash
sam build
```

3.Deploy:
```bash
sam deploy --> This will provide the API Gateway point as output 
```

4.Test Endpoints:
```bash
https://<api-id>.execute-api.<region>.amazonaws.com/Prod/expenses/addExpense --> POST method
https://<api-id>.execute-api.<region>.amazonaws.com/Prod/expenses/getAllExpenses --> Get endpoint
```
---
## Common Errors & Fixes

| Error | Cause | Fix                                                                                                                                                                                  |
|-------|-------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ResourceNotFoundException: Cannot do operations on a non-existent table` | Table does not exist or Lambda cannot reach DynamoDB Local | Ensure table exists, and for SAM Local, use `host.docker.internal:8000` instead of `localhost:8000`. Ensure both local and aws profile use same region. Table name is case sensitive |
| `Connection refused: localhost:8000` | DynamoDB Local not running | Start DynamoDB Local using Docker and ensure container is running                                                                                                                    |
| `MissingAuthenticationTokenException` | API Gateway URL used incorrectly | Use full path including stage, e.g. `https://<api-id>.execute-api.<region>.amazonaws.com/Prod/expenses`                                                                              |
| `403 Forbidden` | Lambda role missing permissions | Add `DynamoDBCrudPolicy` in `template.yaml` to grant Lambda full access                                                                                                              |
| `Unknown output type: JSON` | AWS CLI in PowerShell misinterprets `--output json` | Quote `"json"` or use CMD/Git Bash                                                                                                                                                   | |
| `No active profile set, falling back to default` | `SPRING_PROFILES_ACTIVE` not set | Set via env variable or Maven profile (`-Dspring-boot.run.profiles=local`)                                                                                                           |

---

## Useful tips

- **Local development** uses Spring profile `local` → connects to DynamoDB Local.
- **AWS deployment** uses Spring profile `aws` → connects to AWS DynamoDB.
- SAM Local runs in Docker → must use `host.docker.internal` to reach DynamoDB on host machine.

## Useful Commands

- aws dynamodb delete-table --table-name table-name --endpoint-url http://host.docker.internal:8000
- aws dynamodb scan --table-name Expenses --endpoint-url http://host.docker.internal:8000 --output json
- rm -rf .aws-sam --> To clean local SAM directory
- aws cloudformation rollback-stack --stack-name ExpenseTrackerStack
- aws cloudformation describe-stack-events --stack-name ExpenseTrackerStack --output table
- aws cloudformation delete-stack --stack-name ExpenseTrackerStack
