import axios from 'axios';

export const login = async (mailAddress: string, password: string) => {
  try {
    const response = await axios.post('/api/ems/account/login', {
      email: mailAddress,
      password: password,
    });
    return response;
  } catch (e) {
    throw e;
  }
};

export const logout = async () => {
  try {
    await axios.post('/api/ems/account/logout');
  } catch (e) {
    throw e;
  }
};

export const register = async (mailAddress: string, password: string) => {
  try {
    const response = await axios.post('/api/ems/account/register', {
      email: mailAddress,
      password: password,
    });
    return response;
  } catch (e) {
    throw e;
  }
};
