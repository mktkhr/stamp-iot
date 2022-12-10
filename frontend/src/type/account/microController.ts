import axios from 'axios';

export class MicroController {
  id: number;
  name: string;
  macAddress: string;
  interval: number;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date;
}

export type MicroControllerInfoState = {
  id: number;
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
export const getMicroControllerInfo = async (): Promise<Array<MicroController>> => {
  try {
    const rawResponse = await axios.get('/api/ems/micro-controller/info');
    const response: Array<MicroController> = rawResponse.data;
    return response;
  } catch {
    return null;
  }
};
