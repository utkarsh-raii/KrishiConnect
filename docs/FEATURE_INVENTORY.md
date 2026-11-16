# KrishiConnect - Feature Inventory & Migration Status

Every feature in the application has been catalogued below and classified according to its status in the original MERN project and target status in the Spring Boot migration.

## Feature Classification Legend
- **Fully Implemented**: Working end-to-end in original project.
- **Partially Implemented**: Code exists but lacks complete integration or error handling.
- **Present but Broken**: Code exists with severe bugs, missing models, or broken imports.
- **Missing**: Referenced in UI/specs but absent in backend implementation.
- **Requires Verification**: Complex workflow requiring thorough integration testing.

---

## 1. Authentication & User Management

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| User Registration (Farmer, Buyer, Provider) | Fully Implemented | Complete in Java | Bcrypt password hashing, phone/email validation, role validation. |
| Admin Bootstrap | Partially Implemented | Complete in Java | Auto-bootstrap `admin@fhc.com` upon application startup via `CommandLineRunner`. |
| User Login (Role-gated for Admin) | Fully Implemented | Complete in Java | JWT generation with claims (`id`, `email`, `role`), role verification. |
| Current User Retrieval (`/auth/me`) | Fully Implemented | Complete in Java | Extracted securely from `SecurityContextHolder`. |
| Profile Update (`/auth/profile`) | Fully Implemented | Complete in Java | Updates location, business name, vehicle count, bio, avatar. |
| Password Change (`/auth/change-password`) | Fully Implemented | Complete in Java | Verifies current BCrypt password before persisting new hashed password. |
| User Blocking / Unblocking (Admin) | Fully Implemented | Complete in Java | Prevents login when `is_blocked = true`. |

---

## 2. Farmer Module

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| Farmer Dashboard Metrics | Partially Implemented | Complete in Java | Live metrics for requirements, offers, active listings, earnings. |
| Post Harvest Requirement | Fully Implemented | Complete in Java | Vehicle type, manpower, crop type, quantity, duration, urgency. |
| View My Requirements | Fully Implemented | Complete in Java | Paginated query filtered by authenticated farmer ID. |
| Update / Cancel Requirement | Fully Implemented | Complete in Java | Prevents modification of completed requirements. |
| View Service Bids from Providers | Fully Implemented | Complete in Java | Fetches all provider bids on farmer's requirements. |
| Accept / Reject Provider Bid | Fully Implemented | Complete in Java | Auto-rejects other pending bids when one bid is accepted. |
| Post Crop for Sale (Listing) | Fully Implemented | Complete in Java | Supports image file upload or URL, crop details, price, quantity. |
| View My Crop Listings | Fully Implemented | Complete in Java | Filter by status (`active`, `sold`, `expired`, `withdrawn`). |
| Update / Delete Crop Listing | Fully Implemented | Complete in Java | Only owner farmer can update/delete. |
| View Incoming Buyer Offers | Fully Implemented | Complete in Java | Offers submitted by buyers on listed crops. |
| Respond to Buyer Offer (Accept / Counter / Reject) | Fully Implemented | Complete in Java | Allows counter-offer with revised price and counter-message. |
| Farmer Incoming Orders | Partially Implemented | Complete in Java | View purchase requests, accept/reject status with buyer details. |
| Farmer Feedback Received | Fully Implemented | Complete in Java | Public ratings and testimonials from buyers/providers. |

---

## 3. Buyer Module

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| Browse Active Crop Listings | Fully Implemented | Complete in Java | Public endpoint with search, filter (category, state, price), pagination. |
| View Single Crop Detail | Fully Implemented | Complete in Java | Detailed view with farmer contact, location, harvest season, grade. |
| Submit Offer on Crop Listing | Fully Implemented | Complete in Java | Buyer specifies offer price, quantity, message. |
| View My Submitted Offers | Fully Implemented | Complete in Java | View status (`pending`, `accepted`, `counter`, `rejected`). |
| Direct Purchase from Listing | Present but Broken | Complete in Java | Fixed broken `next()` controller in Node; creates purchase with total calculation. |
| Initiate Purchase from Accepted Offer | Fully Implemented | Complete in Java | Converts accepted offer into confirmed purchase. |
| Upload Payment Receipt | Fully Implemented | Complete in Java | Uploads proof (receipt image/PDF), transitions status to `receipt-uploaded`. |
| View My Orders / Purchases | Fully Implemented | Complete in Java | Filter by payment status (`pending`, `receipt-uploaded`, `verified`, etc.). |
| Buyer Reviews on Crops | Fully Implemented | Complete in Java | Validates that buyer actually purchased the crop before reviewing. |

---

## 4. Service Provider Module

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| Provider Dashboard | Partially Implemented | Complete in Java | Service counts, active bids, confirmed bookings. |
| Create Service Listing | Fully Implemented | Complete in Java | Vehicles (type, count, capacity), manpower (skills), rates, areas. |
| View My Service Listings | Fully Implemented | Complete in Java | Paginated provider services. |
| Update / Delete Service Listing | Fully Implemented | Complete in Java | Provider-scoped updates. |
| Browse Open Farmer Requirements | Fully Implemented | Complete in Java | Filter by crop, vehicle, urgency, state. |
| Submit Bid on Requirement | Fully Implemented | Complete in Java | Unique bid constraint per provider per requirement. |
| View My Bids | Fully Implemented | Complete in Java | Filter by bid status (`pending`, `accepted`, `rejected`, `withdrawn`). |
| Withdraw Bid | Fully Implemented | Complete in Java | Only allowed while bid is `pending`. |
| Confirmed Bookings | Fully Implemented | Complete in Java | Accepted bids treated as active bookings. |
| Complete Booking | Fully Implemented | Complete in Java | Marks bid and parent requirement as `completed`. |

---

## 5. Orders, Cart & Payments

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| Cart Management (`getCart`, `addToCart`, `removeFromCart`) | Present but Broken | Complete in Java | Fixed broken `Product` model reference; linked directly to `CropListing`. |
| Multi-item Order Creation | Present but Broken | Complete in Java | Fixed broken `Product.findById` runtime error; stock checks and total calculation. |
| Razorpay Order Creation | Partially Implemented | Complete in Java | Creates Razorpay order with currency INR, amount in paise, receipt ID. |
| Server-Side Payment Verification | Missing | Complete in Java | Implemented HMAC-SHA256 signature verification (`order_id`, `payment_id`, `signature`). |
| Transaction History | Fully Implemented | Complete in Java | Complete audit trail for offline receipts and online gateway payments. |

---

## 6. Messaging & Chat

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| REST Message History | Present but Broken | Complete in Java | Reconciled route query params (`user1`/`user2` vs `otherUserId`/`cropId`). |
| Send Message | Fully Implemented | Complete in Java | Persists message in MySQL with sender, receiver, crop reference, timestamp. |
| Real-time WebSocket Messaging | Partially Implemented | Complete in Java | Spring WebSocket with STOMP broker for instant peer-to-peer messaging. |

---

## 7. Notifications

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| Notification Retrieval | Present but Broken | Complete in Java | Removed hardcoded user ID (`6a042304...`); derives user from authenticated context. |
| Mark Notification as Read | Present but Broken | Complete in Java | Scoped strictly to authenticated recipient. |
| Trigger System Notifications | Partially Implemented | Complete in Java | Event-driven notification dispatch on new orders, bids, offers, and verifications. |

---

## 8. Reviews & Feedback

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| General User Feedback (`/api/feedback`) | Fully Implemented | Complete in Java | Public ratings for farmers, providers, buyers; auto-calculates rolling rating. |
| Specific Crop Reviews (`/api/reviews`) | Fully Implemented | Complete in Java | Tied to verified purchases; displays on crop detail page. |

---

## 9. Tree Marketplace Module

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| Browse Tree Marketplace | Fully Implemented | Complete in Java | Category filtering (Fruit, Timber, Medicinal, Eco, etc.), pricing, benefits. |
| Admin Post Tree | Fully Implemented | Complete in Java | Admin creates tree profiles with botanical data, images, guides. |
| Delete Tree Listing | Fully Implemented | Complete in Java | Admin deletion. |
| Submit Tree Purchase Request | Fully Implemented | Complete in Java | User specifies quantity, delivery address, phone, calculates total price. |
| View My Tree Requests | Fully Implemented | Complete in Java | User tracks status (`Pending`, `Approved`, `Shipped`, `Delivered`). |
| Admin Manage Tree Requests | Fully Implemented | Complete in Java | Admin updates fulfillment and payment status. |

---

## 10. Admin Module

| Feature | Original Status | Target Migration Status | Notes & Fixes |
|---|---|---|---|
| Admin Dashboard Statistics | Fully Implemented | Complete in Java | Aggregates platform user counts, listings, revenue, 6-month sales series. |
| User Management | Fully Implemented | Complete in Java | Search, filter by role/status, toggle block status, safe deletion. |
| Listing Moderation | Fully Implemented | Complete in Java | Status updates (`active`, `withdrawn`, `expired`), removal of inappropriate listings. |
| Transaction Verification | Fully Implemented | Complete in Java | Verify bank receipts, auto-mark crop listing as `sold`. |
| Dispute Flagging & Resolution | Fully Implemented | Complete in Java | Mark transaction as disputed; resolve with `completed` or `refunded`. |
