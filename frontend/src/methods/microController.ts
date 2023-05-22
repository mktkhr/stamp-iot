import { SpinnerStore } from '@/store/spinnerStore';
import axios from 'axios';

export class MicroController {
  id: number;
  uuid: string;
  name: string;
  macAddress: string;
  interval: number;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date;
}

export type MicroControllerInfoState = {
  id: number;
  uuid: string;
  name: string | null;
  macAddress: string;
  interval: number;
  createdAt: Date;
  updatedAt: Date;
};

export class MicroControllerInfoRequestQuery {
  mailAddress: string;
}

/**
 * アカウントに紐づくマイコン情報取得API
 * @return  アカウントに紐づくマイコン情報 or null
 */
export const microControllerGet = async (): Promise<Array<MicroController>> => {
  try {
    const rawResponse = await axios.get('/api/ems/micro-controller/info');
    const response: Array<MicroController> = rawResponse.data;
    return response;
  } catch (e) {
    throw e;
  }
};

export const microControllerRegister = async (userId: string, macAddress: string) => {
  try {
    await axios.post('/api/ems/micro-controller', {
      userId: userId,
      macAddress: macAddress,
    });
  } catch (e) {
    throw e;
  }
};
