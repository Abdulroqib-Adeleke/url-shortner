# B2B2C SaaS Platform

A multi-tenant Business-to-Business-to-Customer (B2B2C) Software-as-a-Service (SaaS) platform built with Spring Boot 3 and Java 21.

The platform enables a central System Administrator (SysAdmin) to manage platform-wide services, allows Companies (Agents) to onboard and activate approved services, and enables Customers to register under a specific company and consume those services.

A sample service currently implemented is a URL Shortener module.

---

## Architecture Overview

The application follows a multi-tenant B2B2C architecture:

### System Administrator

The SysAdmin manages the platform and controls which services are available for companies to activate.

Responsibilities:

* Create and manage system-wide services
* Enable or disable platform services
* Oversee companies and customers
* Control platform access

### Agent (Company)

Companies register on the platform and act as service providers to their customers.

Responsibilities:

* Create company accounts
* Activate approved services
* Manage company customers
* Provide services to subscribed customers

### Customer

Customers register under a specific company and gain access only to the services offered by that company.

Responsibilities:

* Register with a company
* Login to the platform
* Access company-enabled services
* Manage personal profile

---

# Technology Stack

| Technology         | Version                     |
| ------------------ | --------------------------- |
| Java               | 21                          |
| Spring Boot        | 3.x                         |
| Spring MVC         | 6.x                         |
| Spring Security    | 6.x                         |
| JWT Authentication | JJWT                        |
| Database           | PostgreSQL                  |
| Build Tool         | Maven                       |
| ORM                | Spring Data JPA / Hibernate |
| Validation         | Jakarta Validation          |
| API Format         | REST JSON                   |

---

# Key Features

* Multi-tenant architecture
* JWT Authentication and Authorization
* Role-based access control
* Company management
* Customer onboarding
* Service activation/deactivation
* URL Shortening Service
* PostgreSQL persistence
* Secure API endpoints
* Spring Security integration

---

# User Roles

## SYS_ADMIN

Platform administrator.

Permissions:

* Create system services
* Enable/disable services
* View platform companies
* Manage platform access

---

## ADMIN

Represents a company using the platform.

Permissions:

* Register company account
* Activate services
* Disable services
* Manage customers

---

## CUSTOMER

End user attached to a company.

Permissions:

* Login
* Access enabled services
* Manage profile

---

# Authentication

The application uses JWT (JSON Web Token) authentication.

After successful login, a JWT token is generated and must be supplied in the Authorization header for protected endpoints.

Example:

```http
Authorization: Bearer eyJhbGciOi...
```

---

# Base URL

```text
http://localhost:8080
```

---

# API Documentation

# Authentication APIs

## Welcome Endpoint

```http
GET /api/auth/welcome
```

Public endpoint used to verify application availability.

---

## SysAdmin Signup

```http
POST /api/auth/sysadmin/signup
```

Registers a new system administrator.

---

## Agent Signup

```http
POST /api/auth/agent/signup
```

Registers a new company account.

---

## Customer Signup

```http
POST /api/auth/customer/signup
```

Registers a new customer under a company.

---

## Login

```http
POST /api/auth/login
```

Authenticates SysAdmin or Agent users.

Request Example:

```json
{
  "email": "admin@example.com",
  "password": "Password123!"
}
```

---

## Customer Login

```http
POST /api/auth/customer/login
```

Request Headers:

```http
X-COMPANY_ID: company-uuid
```

Request Body:

```json
{
  "email": "customer@example.com",
  "password": "Password123!"
}
```

---

## Logout

```http
PUT /api/auth/logout
```

Logs out authenticated users.

---

# Company APIs

## Create Company

```http
POST /api/company/create-company
```

Creates a company profile.

---

## Logged-In Company Profile

```http
GET /api/company/company-profile
```

Returns the profile of the authenticated company.

---

## Fetch Company By ID

```http
GET /api/company/company-profile/{id}
```

Returns a company profile by identifier.

---

# Customer APIs

## Customer Profile

```http
GET /api/customer/profile
```

Returns authenticated customer profile information.

---

# Service Offering APIs

## Create Platform Service

```http
POST /api/service/principal/create-service
```

Creates a system-level service available to companies.

Example services:

* URL Shortener
* Analytics
* CRM
* Billing
* Notifications

---

## Disable Platform Service

```http
PUT /api/service/principal/disable-service/{serviceId}
```

Disables a platform-wide service.

---

## Fetch System Services

```http
GET /api/service/fetch-system-service
```

Returns all available platform services.

---

## Agent Create Service

```http
POST /api/service/agent/create-service
```

Allows a company to activate a platform service.

---

## Agent List Services

```http
POST /api/service/agent/list-service
```

Returns services activated by the company.

---

## Agent Disable Service

```http
PUT /api/service/agent/disable-service/{serviceId}
```

Disables a company-specific service.

---

## URL Shortener Module

### Generate Short URL

```http
POST /api/module/urlshortener/generate-shorturl
```

Creates a shortened URL.

Request Example:

```json
{
  "url": "https://www.example.com"
}
```

Response Example:

```json
{
  "shortCode": "ab12cd",
  "shortUrl": "http://localhost:8080/ab12cd"
}
```

---

### Redirect To Original URL

```http
GET /{shortCode}
```

Example:

```http
GET /ab12cd
```

Redirects to the original URL.

---

# Database Configuration

PostgreSQL is used as the primary database.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/b2b2c_db
spring.datasource.username=postgres
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# Running The Application

## Prerequisites

Install:

* Java 21
* Maven 3.9+
* PostgreSQL 14+

---

## Clone Repository

```bash
git clone https://github.com/Abdulroqib-Adeleke/url-shortner.git

cd url-shortner
```

---

## Create Database

```sql
CREATE DATABASE b2b2c_db;
```

---

## Configure Environment

Update:

```properties
src/main/resources/application.properties
```

with your PostgreSQL credentials.

---

## Environment Variables

Update:

```
db
JWT_SECRET
usernames
password
```

---

## Build Application

```bash
mvn clean install
```

---

## Run Application

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/b2b2c-saas.jar
```

---

## Verify Startup

Open:

```text
http://localhost:8080/api/auth/welcome
```

Expected response:

```json
{
  "message": "Welcome to A B2B2C SaaS Platform"
}
```

---

# Security

The application secures protected endpoints using:

* Spring Security
* JWT Authentication
* Role-Based Access Control (RBAC)
* Password Encryption (BCrypt)

---

# Example Business Flow

1. SysAdmin registers.
2. SysAdmin creates platform services.
3. Agent (Company) registers.
4. Company creates company profile.
5. Company activates desired services.
6. Customers register under that company.
7. Customers login.
8. Customers access company-enabled services.
9. URL Shortener service generates shortened URLs.
10. Users access shortened links via generated short codes.

---


# License

This project is intended for educational and commercial SaaS deployment purposes.

Copyright © 2026
All Rights Reserved.

---

# Author

## Adeleke Abdulroqib Ayomiposi
