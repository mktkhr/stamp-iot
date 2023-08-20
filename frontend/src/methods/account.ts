import axios from 'axios';

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
/**
 * アカウント削除API
 */
export const deleteAccount = async () => {
  try{
    const response = await axios.delete('api/ems/account/delete');
    return response;
  }catch(e){
    throw e;
  }
}