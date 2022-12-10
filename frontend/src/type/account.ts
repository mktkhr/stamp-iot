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
