# KrishiConnect — Full-Stack Agricultural Marketplace

KrishiConnect is a digital agricultural platform connecting **Farmers**, **Buyers**, **Logistics Service Providers**, and **Platform Administrators**.

The application has been successfully migrated from a legacy MERN stack into a clean, modern **Java Spring Boot 3 + MySQL + React 18** full-stack modular architecture.

---

## 🏗️ Reorganized Repository Architecture

```text
KrishiConnect/
├── backend/                      # Java Spring Boot 3.3.5 / Java 21 Modular Monolith
│   ├── src/main/java/com/krishiconnect/
│   │   ├── config/               # WebMvc, WebSocket, Security Config
│   │   ├── controller/           # 14 REST Controllers (50+ endpoints)
│   │   ├── dto/                  # Request & Response DTOs
│   │   ├── entity/               # 17 JPA Entities
│   │   ├── exception/            # GlobalExceptionHandler
│   │   ├── repository/           # 17 Spring Data Repositories
│   │   ├── security/             # JWT Provider, AuthFilter, UserDetails
│   │   ├── service/              # 13 Service Interfaces & Implementations
│   │   └── util/                 # FileStorageService
│   ├── src/main/resources/
│   │   └── application.properties # MySQL & App configuration
│   └── pom.xml                   # Maven build configuration (IntelliJ root module)
│
├── frontend/                     # React 18 / Vite SPA
│   ├── public/                   # Static public assets (favicon.svg)
│   ├── src/
│   │   ├── api/                  # Centralized Axios client & API helpers
│   │   ├── components/           # UI components, Modals, Headers, Sidebar
│   │   ├── context/              # AuthContext (JWT & User state)
│   │   ├── layouts/              # DashboardLayout, PublicLayout
│   │   ├── pages/                # Farmer, Buyer, Provider, Admin, Public pages
│   │   └── services/             # STOMP WebSocket client for real-time messaging
│   ├── .env.example              # Frontend environment template
│   └── package.json              # React dependencies
│
├── legacy/                       # Preserved reference backend (isolated from active development)
│   └── farmer-harvest-connect-backend/
│
├── docs/                         # Comprehensive project documentation
│   ├── API_DOCUMENTATION.md      # Complete REST API contract & endpoints
│   ├── DATABASE_DESIGN.md        # MySQL schema design & JPA entity mappings
│   ├── FEATURE_INVENTORY.md      # Feature matrix across roles
│   ├── MIGRATION_ANALYSIS.md     # Architectural migration details
│   ├── MIGRATION_PROGRESS.md     # Migration task tracking
│   ├── CURRENT_PROJECT_STATUS.md # Technical audit & verification
│   └── SETUP_GUIDE.md            # Detailed IntelliJ IDEA & developer setup
│
├── .gitignore                    # Universal multi-stack ignore file
├── .env.example                  # Centralized environment variable template
├── PROGRESS.md                   # Phase 1 Migration & Reorganization Report
└── README.md                     # Main repository guide
```

---

## 🚀 Quick Start Guide

### 1. Prerequisites
- **Java 21** or higher
- **Maven 3.8+** (or bundled with IntelliJ IDEA)
- **Node.js 18+** & **npm**
- **MySQL 8.0+**

[docs/SETUP_GUIDE.md](docs/SETUP_GUIDE.md)(file:///c:/Users/utkar/OneDrive/Desktop/KrishiConnect/docs/SETUP_GUIDE.md).

---

### 2. Database Setup

Create the MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS krishiconnect CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Update credentials in `backend/src/main/resources/application.properties` (or set environment variables):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/krishiconnect?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

On first run, Spring Boot automatically creates all tables and bootstraps default accounts.

---

### 3. Start Backend

#### In IntelliJ IDEA:
1. Open the `backend/` folder directly or open `KrishiConnect` and import `backend/pom.xml`.
2. Run `com.krishiconnect.KrishiConnectApplication`.

#### Via Terminal / Command Line:
```bash
cd backend
mvn spring-boot:run
```
The backend server runs on `http://localhost:5001`.  
Health check: `GET http://localhost:5001/health`

---

### 4. Start Frontend

```bash
cd frontend
npm install
npm run dev
```
The React frontend starts on `http://localhost:5173`.

---

## 🔑 Default Credentials & Roles

| Role | Email | Password |
|---|---|---|
| **Admin** | `admin@fhc.com` | `Admin@1234` |
| **Farmer** | `farmer@demo.com` | `demo123` |
| **Buyer** | `priya@buyer.com` | `Buyer@123` |
| **Service Provider** | `provider@demo.com` | `demo123` |

---

## 🧪 Testing & Verification

### Backend Build & Tests:
```bash
cd backend
mvn clean test
```
Result: **BUILD SUCCESS** (136 source files compiled, 0 test failures, 0 test errors).

### Frontend Production Build:
```bash
cd frontend
npm run build
```
Result: **BUILD SUCCESS** (✓ 1078 modules transformed).

---

## 📚 Documentation Links
- [Setup & Run Guide](docs/SETUP_GUIDE.md)
- [API Documentation](docs/API_DOCUMENTATION.md)
- [Database Design](docs/DATABASE_DESIGN.md)
- [Feature Inventory](docs/FEATURE_INVENTORY.md)
- [Phase 1 Progress Report](PROGRESS.md)
