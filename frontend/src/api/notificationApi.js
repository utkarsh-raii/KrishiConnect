import api from './axios';

export const fetchNotifications = async (token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.get('/notifications', { headers });
  return response.data;
};

export const markNotificationAsRead = async (id, token) => {
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  const response = await api.put(`/notifications/read/${id}`, {}, { headers });
  return response.data;
};
