import axios from 'axios';
import {
  AUTH_UNAUTHORIZED_EVENT,
  clearStoredAuth,
  getStoredToken,
} from '../utils/authStorage';

const api = axios.create({
  baseURL:
    import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  headers: {
    Accept: 'application/json',
  },
  timeout: 30000,
});

api.interceptors.request.use(
  (config) => {
    const token = getStoredToken();

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error),
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const requestUrl = String(error.config?.url || '');
    const isLoginRequest = requestUrl.includes('/Auth/Login');

    if (error.response?.status === 401 && !isLoginRequest) {
      clearStoredAuth();
      window.dispatchEvent(new Event(AUTH_UNAUTHORIZED_EVENT));
    }

    return Promise.reject(error);
  },
);

export function getApiErrorMessage(error) {
  if (error.response) {
    const data = error.response.data;

    if (typeof data === 'string' && data.trim()) {
      return data;
    }

    if (data?.message) {
      return data.message;
    }

    if (data?.title) {
      return data.title;
    }

    if (Array.isArray(data?.errors)) {
      return data.errors.join(', ');
    }

    if (data?.errors && typeof data.errors === 'object') {
      const validationMessages = Object.values(data.errors)
        .flat()
        .filter(Boolean);

      if (validationMessages.length) {
        return validationMessages.join(', ');
      }
    }

    return `Request failed with status ${error.response.status}.`;
  }

  if (error.request) {
    return 'Unable to reach the Spring Boot API. Ensure the backend is running on port 8080.';
  }

  return error.message || 'An unexpected error occurred.';
}

export default api;
