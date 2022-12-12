import axios from 'axios';

export class MeasuredDataState {
  sdi12Data: Array<Sdi12DataState>;
  environmentalData: Array<EnvironmentalDataState>;
  voltageData: Array<VoltageDataState>;
}

export type Sdi12DataState = {
  sdiAddress: string;
  dataList: Array<Sdi12Data>;
};

export type VoltageDataState = {
  measuredDataMasterId: number;
  dayOfYear: number;
  voltage: number;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date;
};

export type Sdi12Data = {
  measuredDataMasterId?: number;
  dayOfYear?: number;
  vwc?: number;
  soilTemp?: number;
  brp?: number;
  sbec?: number;
  spwec?: number;
  gax?: number;
  gay?: number;
  gaz?: number;
  createdAt?: Date;
  updatedAt?: Date;
  deletedAt?: Date;
};

export type EnvironmentalDataState = {
  measuredDataMasterId: number;
  dayOfYear: number;
  airPress: number;
  temp: number;
  humi: number;
  co2Concent: number;
  tvoc: number;
  analogValue: number;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date;
};

/**
 * マイコンIDを指定して測定結果を取得するAPI
 * @return  測定データ or null
 */
export const getMeasuredData = async (
  microControllerId: string
): Promise<MeasuredDataState> => {
  try {
    const rawResponse = await axios.get('/api/ems/measured-data', {
      params: { microControllerId: microControllerId },
    });
    const response: MeasuredDataState = rawResponse.data;
    return response;
  } catch {
    return null;
  }
};
