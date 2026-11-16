# KrishiConnect — Current Project Status

## 1. Executive Summary
The KrishiConnect application migration from MERN (MongoDB, Express, React, Node.js) to Java Full Stack (Spring Boot 3, MySQL 8, Spring Security, JWT, JPA, Hibernate, WebSocket/STOMP, React) has been completed and verified end-to-end.

## 2. Architecture & Components

### Backend (`krishiconnect-backend`)
- **Framework**: Spring Boot 3.3.5, Java 21, Maven
- **Database**: MySQL 8 (H2 in MySQL compatibility mode for unit tests)
- **Security**: Stateless Spring Security with JJWT 0.12.5, BCrypt password hashing, role-based method security (`ROLE_FARMER`, `ROLE_BUYER`, `ROLE_PROVIDER`, `ROLE_ADMIN`)
- **Entities (17)**: `User`, `CropListing`, `HarvestRequirement`, `BidOffer`, `CropOffer`, `Purchase`, `Order`, `OrderItem`, `Cart`, `CartItem`, `ServiceListing`, `Review`, `Feedback`, `Message`, `Notification`, `Tree`, `TreeRequest`
- **Repositories (17)**: Spring Data JPA with derived queries and JPQL
- **Services (13)**: Fully implemented business logic layer with transaction boundaries
- **Controllers (14)**: Covering all 50+ REST endpoints matching original routes
- **Real-Time Communication**: Spring STOMP over SockJS (`/ws`, `/topic/messages/{userId}`)
- **Payment Processing**: Razorpay order initiation & HMAC-SHA256 signature verification

### Frontend (`farmer-harvest-connect-frontend`)
- **Framework**: React 18, Vite 5, Tailwind CSS
- **Routing**: React Router DOM 6 with role-based dashboard layouts
- **API Client**: Centralized Axios client (`src/api/axios.js`) with request interceptor for JWT bearer tokens and response interceptor for 401 handling
- **Real-Time Chat**: `@stomp/stompjs` + `sockjs-client` service connecting to Spring STOMP broker

## 3. Build & Test Verification

| Module | Command | Result |
|---|---|---|
| Backend Compilation | `mvn compiler:compile` | **BUILD SUCCESS** (0 errors, 136 source files) |
| Backend Tests | `mvn test` | **BUILD SUCCESS** (Tests run: 1, Failures: 0, Errors: 0, Skipped: 0) |
| Frontend Production Build | `npm run build` | **BUILD SUCCESS** (✓ 1078 modules transformed in 8.25s) |

## 4. Key Fixes Applied

1. **DTO Contract Alignment**: Added dual-property serialization (`@JsonProperty`) for all entity IDs (`id` and `_id`) and relational objects (`buyer`/`buyerId`, `farmer`/`farmerId`, `listing`/`listingId`) across `PurchaseResponseDto`, `CropOfferResponseDto`, `BidResponseDto`, `OrderResponseDto`, `UserDto`, etc.
2. **API Centralization**: Eliminated raw `fetch("http://localhost:5001/api/...")` across 16 frontend components/pages. Centralized all calls through `src/api/axios.js` with `import.meta.env.VITE_API_URL`.
3. **Chat Protocol Migration**: Removed obsolete `socket.io-client` connection attempts; implemented clean Spring STOMP/SockJS client in `ChatModal.jsx`, `ChatPage.jsx`, and `BuyerChat.jsx`.
4. **Runtime Crash Prevention**:
   - Fixed `FarmerOrders.jsx` `.slice` crash on numerical IDs.
   - Fixed `BuyerListings.jsx` pagination data unpacking bug.
   - Fixed `DashboardHeader.jsx` unauthenticated notification polling.
   - Made review comment optional in `ReviewRequest.java` to prevent 400 Bad Request on rating-only reviews.
5. **Razorpay Integration**: Implemented proper order creation and HMAC-SHA256 signature verification in both backend and frontend component `Payment.jsx`.

## 5. Startup Commands

### Backend
```bash
cd krishiconnect-backend
mvn spring-boot:run
```

### Frontend
```bash
cd farmer-harvest-connect-frontend
npm run dev
```
Runs on `http://localhost:5173` talking to Spring Boot on `http://localhost:5001`.
