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
