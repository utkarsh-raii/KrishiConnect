import api from './axios';

export const addToCart = async (data, token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.post('/cart/add', data, { headers });
  return response.data;
};

export const getCart = async (token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.get('/cart', { headers });
  return response.data;
};

export const removeFromCart = async (productId, token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.delete(`/cart/remove/${productId}`, { headers });
  return response.data;
};
