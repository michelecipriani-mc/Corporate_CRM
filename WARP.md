# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## High-level Architecture

This is a corporate CRM application with a modular monolithic (modulith) architecture.
- The backend is a Java Spring Boot application.
- The frontend is an Angular application.
- The application manages users and permissions at multiple levels.

## Commonly Used Commands

### Backend (`corporate_crm`)

- **Build the project:**
  ```bash
  cd corporate_crm && mvn clean install
  ```
- **Run the application:**
  ```bash
  cd corporate_crm && mvn spring-boot:run
  ```
- **Run tests:**
  ```bash
  cd corporate_crm && mvn test
  ```
- **Clean the project:**
  ```bash
  cd corporate_crm && mvn clean
  ```

### Frontend (`corporate_crm_front`)

- **Install dependencies:**
  ```bash
  cd corporate_crm_front && npm install
  ```
- **Start the development server:**
  ```bash
  cd corporate_crm_front && npm start
  ```
- **Build the project:**
  ```bash
  cd corporate_crm_front && npm run build
  ```
- **Run tests:**
  ```bash
  cd corporate_crm_front && npm test
  ```
