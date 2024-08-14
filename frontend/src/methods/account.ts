import axios, { isAxiosError } from 'axios';

export class Account {
  id: number;
  uuid: string;
  email: string;
  password: string;
  name: string;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date;
}

export class AccountInfoState {
  id: number;
  name?: string | null;
  createdAt: Date;
  updatedAt: Date;
}

export class AccountInfoRequestQuery {
  mailAddress: string;
}

/**
 * アカウント情報取得API
 * @return  アカウント情報 or null
 */
export const getAccountInfo = async (): Promise<AccountInfoState> => {
  try {
    const rawResponse = await axios.get('/api/ems/account/info');
    const response: AccountInfoState = rawResponse.data;
    return response;
  } catch {
    return null;
  }
};

export const login = async (mailAddress: string, password: string) => {
  return await axios.post('/api/ems/account/login', {
    email: mailAddress,
    password: password,
  });
};

export const logout = async () => {
  await axios.post('/api/ems/account/logout');
};

export const register = async (mailAddress: string, password: string) => {
  return await axios.post('/api/ems/account/register', {
    email: mailAddress,
    password: password,
  });
};
/**
 * アカウント削除API
 */
export const deleteAccount = async () => {
  try {
    const response = await axios.delete('api/ems/account/delete');
    return response;
  } catch (e) {
    if (isAxiosError(e)) {
      throw e;
    }
  }
};
