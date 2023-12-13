# banking-application

![image](https://github.com/emirhan190341/banking-application/assets/67711815/7305298a-2a3f-49fe-b36d-1c0edb701214)

In this task, you'll construct a flexible banking service handling numerous transactions for bank accounts within a broader suite of services modeling a simplified banking system.

The focus is on maintaining account owner names, account numbers (String), and balances. The service endpoints will be limited to methods for crediting and debiting the account.

The bank account's data model includes fields for the owner (java.lang.String), account number (String), and balance (double). The credit() method adds a specified amount to the account balance, while the debit() method subtracts the provided amount.

The banking system's object model integrates transaction objects, such as DepositTransaction and WithdrawalTransaction. These transactions keep track of transaction types (deposit, withdrawal, payments), date, and amount. Each transaction type requires specific parameters.

All transactions must include date and amount fields, with the date field automatically calculating the transaction time.

### Technologies

---
- Java 17
- Spring Boot 3.2
- Restful API
- Lombok
- Maven
- Junit5
- Mockito
- Integration Tests
- Docker
- Docker Compose
- CI/CD (Github Actions)
- Postman
- Actuator
- Swagger 3
- PostgreSQL