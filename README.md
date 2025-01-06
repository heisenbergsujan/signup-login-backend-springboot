# Signup and Login backend with email validation Project

This project is a small Spring Boot application that allows users to sign up and activate their accounts via email. It uses Spring Security with JWT-based authentication for secure access and JavaMailSender for sending account activation emails. Postman has been used for API testing.

## Features

- User signup with firstname,lastname,email and password.
- User login with email and password
- Email verification using an activation code.
- Secure JWT-based authentication.
- Tested APIs using Postman.


## Technologies Used

- **Spring Boot**: Backend framework.
- **Spring Security**: For secure authentication and authorization.
- **JWT (JSON Web Token)**: Token-based authentication.
- **JavaMailSender**: For sending activation emails.
- **Postman**: For API testing.

## Setup Instructions

1. **Clone the Repository**

   ```
   git clone <repository-url>
   cd <project-directory>
   ```

2. **Configure Application Properties**


3. **Build and Run the Application**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Test APIs with Postman**

    Test endpoints using postman

   ### Endpoints

   **1. User Signup**
    - **URL**: `/api/auth/signup`
    - **Method**: POST
    - **Body** (JSON):
      ```json
      {
        "email": "user@example.com",
        "password": "securepassword"
      }
      ```

   **2. Account Activation**
    - **URL**: `/api/auth/activate-account`
    - **Method**: GET
    - **Query Params**:
        - `activationCode`: Activation code sent to the email.

   Example:
   ```
   /api/auth/activate?activationCode=123456
   ```

   **3. JWT Login**
    - **URL**: `/api/auth/login`
    - **Method**: POST
    - **Body** (JSON):
      ```json
      {
        "email": "user@example.com",
        "password": "securepassword"
      }
      ```

   **4. Secured Endpoint**
    - **URL**: `/api/<secure(create-other-endpoints)>`
    - **Method**: GET/POST/PUT/DELETE
    - **Headers**:
        - `Authorization`: Bearer `<JWT-Token>`


