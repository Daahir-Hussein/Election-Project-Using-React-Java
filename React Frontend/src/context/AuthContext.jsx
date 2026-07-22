import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import AuthService from '../services/AuthService';
import { getApiErrorMessage } from '../services/api';
import {
  AUTH_UNAUTHORIZED_EVENT,
  AUTH_USER_KEY,
  clearStoredAuth,
  getStoredToken,
  getStoredUser,
  storeAuth,
} from '../utils/authStorage';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(getStoredUser);
  const [isInitializing, setIsInitializing] = useState(
    Boolean(getStoredToken()),
  );

  const clearAuth = useCallback(() => {
    clearStoredAuth();
    setUser(null);
  }, []);

  useEffect(() => {
    const validateStoredSession = async () => {
      if (!getStoredToken()) {
        setIsInitializing(false);
        return;
      }

      try {
        const { data } = await AuthService.me();
        localStorage.setItem(AUTH_USER_KEY, JSON.stringify(data));
        setUser(data);
      } catch {
        clearAuth();
      } finally {
        setIsInitializing(false);
      }
    };

    validateStoredSession();
  }, [clearAuth]);

  useEffect(() => {
    const handleUnauthorized = () => {
      clearAuth();
      setIsInitializing(false);
    };

    window.addEventListener(AUTH_UNAUTHORIZED_EVENT, handleUnauthorized);
    return () =>
      window.removeEventListener(
        AUTH_UNAUTHORIZED_EVENT,
        handleUnauthorized,
      );
  }, [clearAuth]);

  const login = useCallback(async (username, password) => {
    try {
      const { data } = await AuthService.login({
        username: username.trim(),
        password,
      });

      if (!data?.token || !data?.user) {
        return {
          success: false,
          message: 'The server returned an invalid login response.',
        };
      }

      storeAuth(data.token, data.user);
      setUser(data.user);

      return { success: true, user: data.user };
    } catch (error) {
      return {
        success: false,
        message: getApiErrorMessage(error),
      };
    }
  }, []);

  const logout = useCallback(() => {
    clearAuth();
  }, [clearAuth]);

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: Boolean(user && getStoredToken()),
      isInitializing,
      login,
      logout,
    }),
    [user, isInitializing, login, logout],
  );

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }

  return context;
}
