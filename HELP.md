# Bank Application - Getting Started

## Project Structure

### JPA Entities

The application uses JPA for database persistence with the following entities:

#### 1. **User Entity**
- **Fields**: id, email, password, firstName, lastName, role
- **Role**: Enum (ADMIN, USER)
- **Validation**: Email validation, password length, name constraints

#### 2. **Account Entity**
- **Fields**: id, accountNumber, balance, user
- **Relationship**: Many-to-One with User
- **Validation**: Account number format, non-negative balance

#### 3. **Transaction Entity**
- **Fields**: id, amount, timestamp, type, account
- **Type**: Enum (DEBIT, CREDIT)
- **Relationship**: Many-to-One with Account
- **Validation**: Positive amounts, timestamp required

### JPA Repositories

The application uses Spring Data JPA repositories for database operations:

#### 1. **UserRepository**
- `findByEmail(String email)` - Find user by email
- `existsByEmail(String email)` - Check if user exists by email
- Standard CRUD operations (inherited from JpaRepository)

#### 2. **AccountRepository**
- `findByUserId(Long userId)` - Find all accounts for a user
- Standard CRUD operations (inherited from JpaRepository)

#### 3. **TransactionRepository**
- `findByAccountId(Long accountId)` - Find all transactions for an account
- Standard CRUD operations (inherited from JpaRepository)

### Spring Security Configuration

The application includes Spring Security with role-based access control:

#### **SecurityConfig** - Main security configuration
- **BCryptPasswordEncoder** for password encryption
- **Form-based authentication** with custom login page
- **Role-based access control**:
  - `/admin/**` - Only ADMIN role
  - `/user/**` - Authenticated users
  - `/register` - Public access

#### **CustomUserDetailsService** - User authentication
- Loads users from database by email
- Implements Spring Security's UserDetailsService
- Converts User entity to UserDetails for authentication

#### **Access Control**
- **ADMIN endpoints**: `/admin/**`
- **USER endpoints**: `/user/**`
- **Public endpoints**: `/login`, `/register`
- **Authentication**: Email and password
- **Password encoding**: BCrypt

### Database Configuration

- **Database**: MySQL
- **URL**: `jdbc:mysql://localhost:3306/bank_app`
- **Credentials**: root / 123
- **Hibernate**: Auto-creates tables on startup

### Test Data

The application initializes with test data on startup:
- **Admin user**: admin@bank.com (password: password123)
- **Test users**: john.doe@example.com, jane.smith@example.com, bob.wilson@example.com
- **Test accounts and transactions**: Included in data.sql

## Quick Start

1. Ensure MySQL is running on localhost:3306
2. Create database: `CREATE DATABASE bank_app;`
3. Run the application: `mvn spring-boot:run`
4. The database tables will be created automatically
5. Test data will be loaded from data.sql

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.7/reference/web/servlet.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.7/reference/using/devtools.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

