import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:5001/api',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' },
});

// Request interceptor - attach token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('fhc_token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - handle auth errors
api.interceptors.response.use(
  (res) => res,
  (error) => {
    const isAuthRequest = error.config?.url?.includes('/auth/login') || error.config?.url?.includes('/auth/register');
    
    if (error.response?.status === 401 && !isAuthRequest) {
      localStorage.removeItem('fhc_token');
      localStorage.removeItem('fhc_user');
      window.location.href = '/';
    }
    return Promise.reject(error);
  }
);

export default api;

// API helpers
export const cancelOrder = (id) => api.patch(`/orders/${id}/cancel`);
export const confirmDelivery = (id) => api.patch(`/orders/${id}/confirm-delivery`);

export const farmerUpdateOrderStatus = (id, status) => api.patch(`/orders/${id}/status`, { status });
export const farmerApi = {
  register:       (data) => api.post('/auth/register', { ...data, role: 'farmer' }),
  login:          (data) => api.post('/auth/login', { ...data, role: 'farmer' }),
  postHarvest:    (data) => api.post('/farmer/harvest', data),
  getOffers:      ()     => api.get('/farmer/offers'),
  respondOffer:   (id, status) => api.patch(`/farmer/offers/${id}`, { status }),
  postCrop:       (data) => api.post('/farmer/crops', data, { headers: { 'Content-Type': 'multipart/form-data' } }),
  getCrops:       ()     => api.get('/farmer/crops'),
  getBuyerOffers: ()     => api.get('/farmer/buyer-offers'),
  getProfile:     ()     => api.get('/farmer/profile'),
  updateProfile:  (data) => api.put('/farmer/profile', data),
};

export const providerApi = {
  register:      (data) => api.post('/auth/register', { ...data, role: 'provider' }),
  login:         (data) => api.post('/auth/login', { ...data, role: 'provider' }),
  postService:   (data) => api.post('/provider/services', data),
  getServices:   ()     => api.get('/provider/services'),
  deleteService: (id)   => api.delete(`/provider/services/${id}`),
  getRequests:   ()     => api.get('/provider/requests'),
  submitBid:     (data) => api.post('/provider/bids', data),
  getBookings:   ()     => api.get('/provider/bookings'),
  getProfile:    ()     => api.get('/provider/profile'),
};

export const buyerApi = {
  register:     (data)  => api.post('/auth/register', { ...data, role: 'buyer' }),
  login:        (data)  => api.post('/auth/login', { ...data, role: 'buyer' }),
  getCrops:     (params)=> api.get('/buyer/crops', { params }),
  makeOffer:    (data)  => api.post('/buyer/offers', data),
  getOrders:    ()      => api.get('/buyer/orders'),
  uploadReceipt:(id, data) => api.post(`/buyer/orders/${id}/receipt`, data, { headers: { 'Content-Type': 'multipart/form-data' } }),
  getProfile:   ()      => api.get('/buyer/profile'),
};

export const adminApi = {
  login:         (data) => api.post('/auth/login', { ...data, role: 'admin' }),
  getStats:      ()     => api.get('/admin/stats'),
  getUsers:      (params) => api.get('/admin/users', { params }),
  blockUser:     (id)   => api.patch(`/admin/users/${id}/block`),
  getTransactions:()    => api.get('/admin/transactions'),
  getDisputes:   ()     => api.get('/admin/disputes'),
  getListings:   ()     => api.get('/admin/listings'),
  removeListings:(id)   => api.delete(`/admin/listings/${id}`),
};

export const treeApi = {
  getTrees:        () => api.get('/trees'),
  getTreeById:     (id) => api.get(`/trees/${id}`),
  requestTree:     (data) => api.post('/trees/request', data),
  getMyRequests:   () => api.get('/trees/my-requests'),
  adminGetTrees:   () => api.get('/admin/trees'),
  adminPostTree:   (data) => api.post('/admin/trees', data, { headers: { 'Content-Type': 'multipart/form-data' } }),
  adminDeleteTree: (id) => api.delete(`/admin/trees/${id}`),
  adminGetRequests:() => api.get('/admin/tree-requests'),
  adminUpdateStatus:(id, status, paymentStatus) => api.patch(`/admin/tree-requests/${id}/status`, { status, paymentStatus }),
};

export const messageApi = {
  getMessages:     (params) => api.get('/messages', { params }),
  sendMessage:     (data) => api.post('/messages', data),
};

export const reviewApi = {
  addReview:       (data) => api.post('/reviews/add', data),
  getCropReviews:  (cropId) => api.get(`/reviews/${cropId}`),
};

export const feedbackApi = {
  submitFeedback:  (data) => api.post('/feedback', data),
  getUserFeedback: (userId, params) => api.get(`/feedback/user/${userId}`, { params }),
  getMyFeedback:   (params) => api.get('/feedback/my', { params }),
  deleteFeedback:  (id) => api.delete(`/feedback/${id}`),
};
