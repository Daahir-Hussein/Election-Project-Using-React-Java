import api from './api';

const AuthService = {
  login(credentials) {
    return api.post('/Auth/Login', credentials);
  },

  register(payload) {
    return api.post('/Auth/Register', payload);
  },

  me() {
    return api.get('/Auth/Me');
  },
};

export default AuthService;
