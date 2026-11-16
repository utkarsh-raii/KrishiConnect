import api from './axios';

export const initiatePayment = async (amount, token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.post('/payments/create-order', { amount }, { headers });
  return response.data;
};

export const verifyPayment = async (paymentData, token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.post('/payments/verify', paymentData, { headers });
  return response.data;
};
