# KrishiConnect — Full API Integration & Contract Status Matrix

This document maps every REST API endpoint in the Spring Boot backend to its controller, request/response DTOs, security/role requirements, and corresponding React frontend components.

---

## 1. Authentication & User Management (`/api/auth`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `POST` | `/api/auth/register` | Public | `AuthController.register` | `RegisterRequest` (`name`, `email`, `phone`, `password`, `role`, `profile`) | `{ success, message, token, user, data: { token, user } }` | `FarmerAuth.jsx`, `BuyerAuth.jsx`, `ProviderAuth.jsx` |
| `POST` | `/api/auth/login` | Public | `AuthController.login` | `LoginRequest` (`email`, `password`, `role`) | `{ success, message, token, user, data: { token, user } }` | `FarmerAuth.jsx`, `BuyerAuth.jsx`, `ProviderAuth.jsx`, `AdminLogin.jsx` |
| `GET` | `/api/auth/me` | Authenticated | `AuthController.getCurrentUser` | None | `{ success, user, data: user }` | `AuthContext.jsx`, `DashboardHeader.jsx` |
| `PUT` | `/api/auth/profile` | Authenticated | `AuthController.updateProfile` | `UpdateProfileRequest` | `{ success, message, user, data: user }` | `FarmerProfile.jsx`, `BuyerProfile.jsx`, `Profile.jsx` |
| `PUT` | `/api/auth/change-password` | Authenticated | `AuthController.changePassword` | `ChangePasswordRequest` | `{ success, message }` | Profile pages |

---

## 2. Farmer Operations (`/api/farmer`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `POST` | `/api/farmer/harvest`, `/api/farmer/requirements` | `ROLE_FARMER` | `FarmerController.postRequirement` | `HarvestRequirementRequest` | `{ success, message, data: HarvestRequirementResponse }` | `PostHarvest.jsx` |
| `GET` | `/api/farmer/requirements`, `/api/farmer/requirements/my` | `ROLE_FARMER` | `FarmerController.getMyRequirements` | `status`, `page`, `limit` | `{ success, data: List<HarvestRequirementResponse>, pagination }` | `Dashboard.jsx` (Farmer) |
| `PATCH` | `/api/farmer/requirements/{id}` | `ROLE_FARMER` | `FarmerController.updateRequirement` | `{ status, notes }` | `{ success, message, data: HarvestRequirementResponse }` | `Dashboard.jsx` (Farmer) |
| `GET` | `/api/farmer/offers` | `ROLE_FARMER` | `FarmerController.getMyOffers` | `status`, `page`, `limit` | `{ success, data: List<BidResponseDto>, pagination }` | `FarmerOffers.jsx` |
| `PATCH` | `/api/farmer/offers/{id}` | `ROLE_FARMER` | `FarmerController.respondToOffer` | `{ status }` | `{ success, message, data: BidResponseDto }` | `FarmerOffers.jsx` |
| `POST` | `/api/farmer/crop-listing`, `/api/farmer/crops` | `ROLE_FARMER` | `FarmerController.createCropListingJson` / `createCropListingMultipart` | `CropListingRequest` (JSON or multipart image) | `{ success, message, data: CropListingResponse }` | `FarmerCrops.jsx` |
| `GET` | `/api/farmer/crop-listing/my`, `/api/farmer/crops` | `ROLE_FARMER` | `FarmerController.getMyCropListings` | None | `{ success, data: List<CropListingResponse> }` | `FarmerCrops.jsx` |
| `PATCH` | `/api/farmer/crop-listing/{id}` | `ROLE_FARMER` | `FarmerController.updateCropListingJson` | `CropListingRequest` | `{ success, message, data: CropListingResponse }` | `FarmerCrops.jsx` |
| `DELETE` | `/api/farmer/crop-listing/{id}` | `ROLE_FARMER` | `FarmerController.deleteCropListing` | None | `{ success, message }` | `FarmerCrops.jsx` |
| `GET` | `/api/farmer/buyer-offers` | `ROLE_FARMER` | `FarmerController.getBuyerOffers` | `status`, `page`, `limit` | `{ success, data: List<CropOfferResponseDto>, pagination }` | `FarmerBuyerOffers.jsx` |
| `PATCH` | `/api/farmer/buyer-offers/{offerId}` | `ROLE_FARMER` | `FarmerController.respondToCropOffer` | `{ status, counterPrice, counterMessage }` | `{ success, message, data: CropOfferResponseDto }` | `FarmerBuyerOffers.jsx` |
| `GET` | `/api/farmer/profile` | `ROLE_FARMER` | `FarmerController.getProfile` | None | `{ success, data: UserDto, user: UserDto }` | `FarmerProfile.jsx` |
| `PUT` | `/api/farmer/profile` | `ROLE_FARMER` | `FarmerController.updateProfile` | `UpdateProfileRequest` | `{ success, data: UserDto, user: UserDto }` | `FarmerProfile.jsx` |

---

## 3. Farmer Orders & Purchase Requests (`/api/farmer-orders`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `GET` | `/api/farmer-orders` | `ROLE_FARMER` | `FarmerOrderController.getPurchaseRequests` | None | `{ success: true, requests: List<PurchaseResponseDto>, data: List<PurchaseResponseDto> }` | `FarmerOrders.jsx` |
| `PUT` / `PATCH` | `/api/farmer-orders/{id}` | `ROLE_FARMER` | `FarmerOrderController.updateRequestStatus` | `{ status }` | `{ success: true, message, request: PurchaseResponseDto, data: PurchaseResponseDto }` | `FarmerOrders.jsx` |

---

## 4. Buyer Operations (`/api/buyer`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `GET` | `/api/buyer/listings`, `/api/buyer/crops` | Public | `BuyerController.getCropListings` | `search`, `category`, `grade`, `season`, `minPrice`, `maxPrice`, `page`, `limit` | `{ success, data: List<CropListingResponse>, pagination }` | `BuyerListings.jsx`, `Home.jsx` |
| `GET` | `/api/buyer/crop-listings/{id}` | Public | `BuyerController.getCropListingById` | `id` | `{ success, data: CropListingResponse }` | `BuyerListings.jsx` |
| `POST` | `/api/buyer/make-offer`, `/api/buyer/offers` | `ROLE_BUYER` | `BuyerController.makeOffer` | `MakeOfferRequest` | `{ success, message, data: CropOfferResponseDto }` | `BuyerListings.jsx` |
| `GET` | `/api/buyer/my-offers` | `ROLE_BUYER` | `BuyerController.getMyOffers` | None | `{ success, data: List<CropOfferResponseDto> }` | Buyer Dashboard |
| `POST` | `/api/buyer/direct-purchase` | `ROLE_BUYER` | `BuyerController.directPurchase` | `DirectPurchaseRequest` (`listingId`, `quantityAmount`, `notes`) | `{ success, message, data: PurchaseResponseDto }` | `BuyerListings.jsx` |
| `POST` | `/api/buyer/purchase` | `ROLE_BUYER` | `BuyerController.initiatePurchase` | `InitiatePurchaseRequest` | `{ success, message, data: PurchaseResponseDto }` | Buyer checkout |
| `POST` | `/api/buyer/orders/{id}/receipt` | `ROLE_BUYER` | `BuyerController.uploadReceipt` | Multipart `receipt` file | `{ success, message, data: PurchaseResponseDto }` | `BuyerOrders.jsx` |
| `GET` | `/api/buyer/orders` | `ROLE_BUYER` | `BuyerController.getMyOrders` | None | `{ success, data: List<PurchaseResponseDto>, orders: List<PurchaseResponseDto> }` | `BuyerOrders.jsx` |
| `GET` | `/api/buyer/orders/{id}` | `ROLE_BUYER` | `BuyerController.getOrderById` | `id` | `{ success, data: PurchaseResponseDto, order: PurchaseResponseDto }` | `BuyerOrders.jsx` |
| `GET` | `/api/buyer/profile` | `ROLE_BUYER` | `BuyerController.getProfile` | None | `{ success, data: UserDto, user: UserDto }` | `BuyerProfile.jsx` |
| `PUT` | `/api/buyer/profile` | `ROLE_BUYER` | `BuyerController.updateProfile` | `UpdateProfileRequest` | `{ success, data: UserDto, user: UserDto }` | `BuyerProfile.jsx` |

---

## 5. Service Provider Operations (`/api/provider`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `POST` | `/api/provider/services` | `ROLE_PROVIDER` | `ProviderController.createService` | `ServiceListingRequest` | `{ success, message, data: ServiceListingResponse }` | `PostService.jsx` |
| `GET` | `/api/provider/services` | `ROLE_PROVIDER` | `ProviderController.getMyServices` | None | `{ success, data: List<ServiceListingResponse> }` | `Dashboard.jsx` (Provider) |
| `DELETE` | `/api/provider/services/{id}` | `ROLE_PROVIDER` | `ProviderController.deleteService` | `id` | `{ success, message }` | `Dashboard.jsx` (Provider) |
| `GET` | `/api/provider/requests` | `ROLE_PROVIDER` | `ProviderController.getAvailableRequirements` | `cropType`, `vehicleType`, `status`, `page`, `limit` | `{ success, data: List<HarvestRequirementResponse>, pagination }` | `FarmerRequests.jsx` |
| `POST` | `/api/provider/bids` | `ROLE_PROVIDER` | `ProviderController.submitBid` | `SubmitBidRequest` | `{ success, message, data: BidResponseDto }` | `FarmerRequests.jsx` |
| `GET` | `/api/provider/bookings` | `ROLE_PROVIDER` | `ProviderController.getMyBookings` | `status`, `page`, `limit` | `{ success, data: List<BidResponseDto>, pagination }` | `Bookings.jsx` |
| `GET` | `/api/provider/profile` | `ROLE_PROVIDER` | `ProviderController.getProfile` | None | `{ success, data: UserDto, user: UserDto }` | `Profile.jsx` (Provider) |
| `PUT` | `/api/provider/profile` | `ROLE_PROVIDER` | `ProviderController.updateProfile` | `UpdateProfileRequest` | `{ success, data: UserDto, user: UserDto }` | `Profile.jsx` (Provider) |

---

## 6. Cart & Order Operations (`/api/cart`, `/api/orders`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `GET` | `/api/cart` | Authenticated | `CartController.getCart` | None | `{ success, cart: CartResponseDto, data: CartResponseDto }` | `BuyerCart.jsx`, `cartApi.js` |
| `POST` | `/api/cart/add` | Authenticated | `CartController.addToCart` | `AddToCartRequest` (`productId`, `quantity`) | `{ success, message, cart: CartResponseDto, data: CartResponseDto }` | `cartApi.js` |
| `DELETE` | `/api/cart/remove/{productId}` | Authenticated | `CartController.removeFromCart` | `productId` | `{ success, message, cart: CartResponseDto, data: CartResponseDto }` | `cartApi.js` |
| `POST` | `/api/orders`, `/api/orders/create` | Authenticated | `OrderController.createOrder` | `CreateOrderRequest` | `{ success, message, order: OrderResponseDto, data: OrderResponseDto }` | `orderApi.js` |
| `GET` | `/api/orders` | Authenticated | `OrderController.getMyOrders` | None | `{ success, orders: List<OrderResponseDto>, data: List<OrderResponseDto> }` | `orderApi.js` |
| `GET` | `/api/orders/{id}` | Authenticated | `OrderController.getOrderById` | `id` | `{ success, order: OrderResponseDto, data: OrderResponseDto }` | `orderApi.js` |
| `PATCH` | `/api/orders/{id}/status` | Authenticated | `OrderController.updateOrderStatus` | `{ status }` | `{ success, message, order: OrderResponseDto, data: OrderResponseDto }` | Order management |

---

## 7. Payments (`/api/payments`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `POST` | `/api/payments/create-order` | Authenticated | `PaymentController.createOrder` | `PaymentOrderRequest` (`amount`, `purchaseId`) | `{ success, orderId, razorpayOrderId, amount, currency, keyId }` | `Payment.jsx`, `paymentApi.js` |
| `POST` | `/api/payments/verify` | Authenticated | `PaymentController.verifyPayment` | `PaymentVerificationRequest` (`razorpayOrderId`, `razorpayPaymentId`, `razorpaySignature`, `purchaseId`) | `{ success, message, purchase }` | `Payment.jsx`, `paymentApi.js` |

---

## 8. Real-Time Chat & Messages (`/api/messages` & `/ws`)

| Protocol | Destination / Endpoint | Access | Controller / Handler | Request Payload | Response / Broadcast Destination | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `REST GET` | `/api/messages` | Authenticated | `MessageController.getMessages` | `otherUserId`, `cropId` | `{ success, data: List<MessageDto>, messages: List<MessageDto> }` | `ChatPage.jsx`, `ChatModal.jsx`, `BuyerChat.jsx` |
| `REST POST` | `/api/messages` | Authenticated | `MessageController.sendMessage` | `SendMessageRequest` (`receiverId`, `cropId`, `message`) | `{ success, message, data: MessageDto }` | `ChatPage.jsx`, `ChatModal.jsx`, `BuyerChat.jsx` |
| `STOMP / SockJS` | `/ws` | Public handshake | `WebSocketConfig` | CONNECT frame with auth token | Connected | `ChatPage.jsx`, `ChatModal.jsx`, `BuyerChat.jsx` |
| `STOMP Sub` | `/topic/messages/{userId}` | Authenticated | `MessageServiceImpl` | Subscription to receiver user ID | Real-time `MessageDto` JSON payload | `ChatPage.jsx`, `ChatModal.jsx`, `BuyerChat.jsx` |

---

## 9. Notifications (`/api/notifications`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `GET` | `/api/notifications` | Authenticated | `NotificationController.getMyNotifications` | None | `{ success, notifications: List<NotificationDto>, data: List<NotificationDto> }` | `DashboardHeader.jsx`, `notificationApi.js` |
| `PUT` | `/api/notifications/read/{id}` | Authenticated | `NotificationController.markAsRead` | `id` | `{ success, message }` | `DashboardHeader.jsx`, `notificationApi.js` |

---

## 10. Reviews & Feedback (`/api/reviews`, `/api/feedback`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `POST` | `/api/reviews/add` | Authenticated | `ReviewController.addReview` | `ReviewRequest` (`cropId`, `purchaseId`, `rating`, `comment`) | `{ success, message, review: ReviewResponseDto, data: ReviewResponseDto }` | `RatingModal.jsx`, `BuyerReviews.jsx` |
| `GET` | `/api/reviews/{cropId}` | Public | `ReviewController.getCropReviews` | `cropId` | `{ success, reviews: List<ReviewResponseDto>, data: List<ReviewResponseDto> }` | `BuyerReviews.jsx` |
| `POST` | `/api/feedback` | Authenticated | `FeedbackController.submitFeedback` | `FeedbackRequest` (`toUser`, `rating`, `comment`, `referenceType`, `referenceId`) | `{ success, message, feedback: FeedbackResponseDto, data: FeedbackResponseDto }` | `RatingModal.jsx`, `FarmerFeedback.jsx`, `Feedback.jsx` |
| `GET` | `/api/feedback/user/{userId}` | Public | `FeedbackController.getUserFeedback` | `userId`, `page`, `limit` | `{ success, data: List<FeedbackResponseDto>, pagination }` | Feedback lists |
| `GET` | `/api/feedback/my` | Authenticated | `FeedbackController.getMyFeedback` | `page`, `limit` | `{ success, data: List<FeedbackResponseDto>, pagination }` | `FarmerFeedback.jsx`, `Feedback.jsx` |
| `DELETE` | `/api/feedback/{id}` | Authenticated | `FeedbackController.deleteFeedback` | `id` | `{ success, message }` | Feedback management |

---

## 11. Tree Marketplace (`/api/trees`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `GET` | `/api/trees` | Public | `TreeController.getTrees` | None | `{ success, trees: List<TreeDto>, data: List<TreeDto> }` | `TreeExplorer.jsx` |
| `GET` | `/api/trees/{id}` | Public | `TreeController.getTreeById` | `id` | `{ success, data: TreeDto }` | `TreeExplorer.jsx` |
| `POST` | `/api/trees/request` | Authenticated | `TreeController.createTreeRequest` | `SubmitTreePurchaseRequest` (`treeId`, `quantity`, `fullName`, `phone`, `shippingAddress`) | `{ success, message, request: TreeRequestDto, data: TreeRequestDto }` | `TreeExplorer.jsx` |
| `GET` | `/api/trees/my-requests` | Authenticated | `TreeController.getMyTreeRequests` | None | `{ success, requests: List<TreeRequestDto>, data: List<TreeRequestDto> }` | `TreeExplorer.jsx` |

---

## 12. Admin Administration (`/api/admin`)

| Method | Endpoint | Access | Controller | Request DTO / Params | Response Shape | Frontend Components / Callers |
|---|---|---|---|---|---|---|
| `GET` | `/api/admin/dashboard-stats`, `/api/admin/stats` | `ROLE_ADMIN` | `AdminController.getDashboardStats` | None | `{ success, data: DashboardStatsDto, stats: DashboardStatsDto }` | `Dashboard.jsx` (Admin) |
| `GET` | `/api/admin/users` | `ROLE_ADMIN` | `AdminController.getUsers` | `role`, `isBlocked`, `search`, `page`, `limit` | `{ success, data: List<UserDto>, users: List<UserDto>, pagination }` | `Users.jsx` |
| `GET` | `/api/admin/users/{id}` | `ROLE_ADMIN` | `AdminController.getUserById` | `id` | `{ success, data: UserDto }` | `Users.jsx` |
| `PUT`/`PATCH` | `/api/admin/users/{id}/block`, `/block-user/{id}` | `ROLE_ADMIN` | `AdminController.toggleBlockUser` | `id` | `{ success, message, data: UserDto }` | `Users.jsx` |
| `DELETE` | `/api/admin/users/{id}` | `ROLE_ADMIN` | `AdminController.deleteUser` | `id` | `{ success, message }` | `Users.jsx` |
| `GET` | `/api/admin/transactions` | `ROLE_ADMIN` | `AdminController.getTransactions` | `status`, `paymentMethod`, `page`, `limit` | `{ success, data: List<PurchaseResponseDto>, transactions: List<PurchaseResponseDto>, pagination }` | `Transactions.jsx` |
| `PATCH` | `/api/admin/transactions/{id}/verify` | `ROLE_ADMIN` | `AdminController.verifyTransaction` | `id` | `{ success, message, data: PurchaseResponseDto }` | `Transactions.jsx` |
| `GET` | `/api/admin/listings` | `ROLE_ADMIN` | `AdminController.getListings` | `status`, `category`, `search`, `page`, `limit` | `{ success, data: List<CropListingResponse>, listings: List<CropListingResponse>, pagination }` | `Listings.jsx` |
| `DELETE` | `/api/admin/listings/{id}` | `ROLE_ADMIN` | `AdminController.deleteListing` | `id` | `{ success, message }` | `Listings.jsx` |
| `GET` | `/api/admin/disputes` | `ROLE_ADMIN` | `AdminController.getDisputes` | `page`, `limit` | `{ success, data: List<PurchaseResponseDto>, disputes: List<PurchaseResponseDto>, pagination }` | `Disputes.jsx` |
| `PATCH` | `/api/admin/disputes/{id}/resolve` | `ROLE_ADMIN` | `AdminController.resolveDispute` | `{ resolution }` | `{ success, message, data: PurchaseResponseDto }` | `Disputes.jsx` |
| `GET` | `/api/admin/trees` | `ROLE_ADMIN` | `AdminController.getTrees` | None | `{ success, data: List<TreeDto>, trees: List<TreeDto> }` | `TreePosting.jsx` |
| `POST` | `/api/admin/trees` | `ROLE_ADMIN` | `AdminController.postTreeMultipart` / `postTreeJson` | Form data / multipart image | `{ success, message, data: TreeDto }` | `TreePosting.jsx` |
| `DELETE` | `/api/admin/trees/{id}` | `ROLE_ADMIN` | `AdminController.deleteTree` | `id` | `{ success, message }` | `TreePosting.jsx` |
| `GET` | `/api/admin/tree-requests` | `ROLE_ADMIN` | `AdminController.getAllTreeRequests` | None | `{ success, data: List<TreeRequestDto>, requests: List<TreeRequestDto> }` | `TreeRequests.jsx` |
| `PATCH` | `/api/admin/tree-requests/{id}/status` | `ROLE_ADMIN` | `AdminController.updateTreeRequestStatus` | `{ status, paymentStatus }` | `{ success, message, data: TreeRequestDto }` | `TreeRequests.jsx` |
