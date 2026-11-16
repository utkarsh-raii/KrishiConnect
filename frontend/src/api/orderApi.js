import api from './axios';

export const createOrder = async (orderData, token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.post('/orders/create', orderData, { headers });
  return response.data;
};

export const getOrders = async (token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.get('/orders', { headers });
  return response.data;
};

export const getOrderById = async (id, token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.get(`/orders/${id}`, { headers });
  return response.data;
};
