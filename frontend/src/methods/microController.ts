import axios from 'axios';

export class MicroController {
  id: number;
  uuid: string;
  name: string;
  macAddress: string;
  interval: number;
  sdi12Address: string;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date | null;
}

export type MicroControllerInfoState = {
  id: number;
  uuid: string;
  name: string | null;
  macAddress: string;
  interval: number;
  sdi12Address: string;
  createdAt: Date;
  updatedAt: Date;
};

export class MicroControllerInfoRequestQuery {
  mailAddress: string;
}

export type MicroControllerDetailPatchParam = {
  microControllerUuid: string;
  name?: string;
  interval?: string;
  sdi12Address?: string;
};

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

/**
 * マイコン詳細取得API
 * @param microControllerUuid マイコンUUID
 */
export const microControllerDetailGet = async (
  microControllerUuid: string
): Promise<MicroController> => {
  try {
    const rawResponse = await axios.get('/api/ems/micro-controller/detail', {
      params: { microControllerUuid: microControllerUuid },
    });
    const response: MicroController = rawResponse.data;
    return response;
  } catch (e) {
    throw e;
  }
};

/**
 * マイコン詳細更新API
 * @param microControllerUuid マイコンUUID
 */
export const microControllerDetailPatch = async (
  param: MicroControllerDetailPatchParam
): Promise<MicroController> => {
  try {
    const rawResponse = await axios.patch('/api/ems/micro-controller/detail', param);
    const response: MicroController = rawResponse.data;
    return response;
  } catch (e) {
    throw e;
  }
};
