export const AUTH_TOKEN_KEY = 'ems_access_token';
export const AUTH_USER_KEY = 'ems_auth_user';
export const AUTH_UNAUTHORIZED_EVENT = 'ems:unauthorized';

export function getStoredToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY);
}

export function getStoredUser() {
  try {
    const value = localStorage.getItem(AUTH_USER_KEY);
    return value ? JSON.parse(value) : null;
  } catch {
    return null;
  }
}

export function storeAuth(token, user) {
  localStorage.setItem(AUTH_TOKEN_KEY, token);
  localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user));
}

export function clearStoredAuth() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  localStorage.removeItem(AUTH_USER_KEY);
}
