# KrishiConnect — Developer Setup & Run Guide

This guide provides end-to-end instructions for setting up, building, and running **KrishiConnect** locally using **IntelliJ IDEA** for the Java backend and **Vite / React** for the frontend.

---

## 1. Prerequisites

Before beginning, ensure the following software is installed on your workstation:

| Component | Minimum Version | Recommended Version | Verification Command |
| :--- | :--- | :--- | :--- |
| **Java Development Kit (JDK)** | Java 21 | Eclipse Temurin 21 or Oracle JDK 21 | `java -version` |
| **Apache Maven** | Maven 3.8+ | Bundled with IntelliJ IDEA | `mvn -v` |
| **MySQL Server** | MySQL 8.0 | MySQL 8.0+ | `mysql --version` |
| **Node.js** | Node 18+ | Node 20 LTS | `node -v` |
| **npm** | npm 9+ | npm 10+ | `npm -v` |
| **IntelliJ IDEA** | Community or Ultimate | 2023.3+ / 2024.1+ | — |

---

## 2. Repository Layout

```text
KrishiConnect/
├── backend/                      # Spring Boot 3.3.5 / Java 21 Modular Monolith
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/krishiconnect/
│   │   │   └── resources/application.properties
│   │   └── test/
│   └── pom.xml                   # Maven build descriptor (root of backend module)
├── frontend/                     # React 18 / Vite SPA
│   ├── public/                   # Static public assets (favicon, icons)
│   ├── src/                      # Components, pages, context, and API clients
│   ├── package.json              # Frontend npm dependencies
│   ├── package-lock.json
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── .env.example
├── legacy/                       # Archived original Node.js/Express/MongoDB backend
│   └── farmer-harvest-connect-backend/
├── docs/                         # Architectural and API documentation
│   ├── API_DOCUMENTATION.md
│   ├── DATABASE_DESIGN.md
│   ├── FEATURE_INVENTORY.md
│   ├── MIGRATION_ANALYSIS.md
│   ├── MIGRATION_PROGRESS.md
│   └── SETUP_GUIDE.md
├── .gitignore                    # Comprehensive multi-language ignore file
├── .env.example                  # Environment configuration template
├── PROGRESS.md                   # Phase 1 Migration and Reorganization Report
└── README.md                     # Main project overview and quickstart
```

---

## 3. Database Setup (MySQL 8)

1. Open your MySQL client (e.g., MySQL Workbench or MySQL CLI):
   ```sql
   CREATE DATABASE IF NOT EXISTS krishiconnect CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Verify MySQL user privileges:
   Ensure your MySQL user (default `root`) has read/write privileges on the `krishiconnect` database.

*(Note: Spring Boot will automatically manage table schemas via Hibernate `ddl-auto=update` upon initial launch.)*

---

## 4. Opening and Running Backend in IntelliJ IDEA

### Step 4.1: Import Project into IntelliJ
You can open the backend in one of two ways:

- **Method A (Recommended - Backend Only)**:
  1. Launch IntelliJ IDEA.
  2. Select **File > Open...** (or **Open** from welcome dialog).
  3. Browse to `KrishiConnect/backend` and select the `backend` folder.
  4. IntelliJ will detect `pom.xml` and import the project as a Maven project automatically.

- **Method B (Full Monorepo)**:
  1. Open the root `KrishiConnect` directory in IntelliJ IDEA.
  2. Open the **Maven** tool window on the right sidebar.
  3. Click the `+` (Add Maven Projects) icon and select `backend/pom.xml`.

### Step 4.2: Configure Project SDK
1. Go to **File > Project Structure... > Project**.
2. Set **SDK** to **21** (e.g., `temurin-21` or `corretto-21`).
3. Ensure **Language Level** is set to **SDK default** or **21**.

### Step 4.3: Configure Database Credentials
You can configure credentials in `backend/src/main/resources/application.properties` or as environment variables:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/krishiconnect?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

Alternatively, define run configuration environment variables:
- `SPRING_DATASOURCE_USERNAME=root`
- `SPRING_DATASOURCE_PASSWORD=your_mysql_password`

### Step 4.4: Run Application
1. In the Project pane, navigate to:
   `src/main/java/com/krishiconnect/KrishiConnectApplication.java`
2. Right-click `KrishiConnectApplication.java` and click **Run 'KrishiConnectApplication'** (or `Shift + F10`).
3. The server will start on port **5001**:
   ```text
   Tomcat started on port 5001 (http) with context path '/'
   KrishiConnectApplication started
   ```
4. On first startup, the backend automatically seeds the default administrator:
   - **Email**: `admin@fhc.com`
   - **Password**: `Admin@1234`

### Step 4.5: Run Backend Tests
- In IntelliJ: Right-click `src/test/java` and select **Run 'All Tests'**.
- From command line:
  ```powershell
  cd backend
  mvn test
  ```

---

## 5. Setting Up and Running Frontend

### Step 5.1: Install Dependencies
```powershell
cd frontend
npm install
```

### Step 5.2: Configure Environment Variables
Copy `.env.example` to `.env`:
```powershell
cp .env.example .env
```
Ensure `frontend/.env` contains:
```env
VITE_API_URL=http://localhost:5001/api
VITE_WS_URL=http://localhost:5001/ws
VITE_RAZORPAY_KEY_ID=rzp_test_placeholder
```

### Step 5.3: Start Vite Development Server
```powershell
npm run dev
```
The application will launch on `http://localhost:5173`.

### Step 5.4: Build Frontend for Production
```powershell
npm run build
```
Build artifacts will be emitted to `frontend/dist/`.

---

## 6. End-to-End Verification Checklist

1. [x] **Backend Compiles**: `mvn clean compile -f backend/pom.xml` passes.
2. [x] **Backend Tests Pass**: `mvn test -f backend/pom.xml` passes with H2 database integration.
3. [x] **Frontend Builds**: `npm run build --prefix frontend` succeeds without syntax or bundle errors.
4. [x] **API Endpoints Ready**: REST controllers available under `/api/*`.
5. [x] **WebSocket STOMP Broker**: Ready on `/ws` endpoint with SockJS fallback.
6. [x] **IntelliJ Compatibility**: Standard Maven root at `backend/pom.xml`.
