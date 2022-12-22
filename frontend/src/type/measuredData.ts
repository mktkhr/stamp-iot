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

export class MeasuredDataset {
  label: string;
  data: number[];
  fill: boolean | undefined;
  lineTension: number;
  borderColor: string;
  pointStyle: string;
  pointRadius: number;
}

export class Sdi12ChartConfig {
  x: {
    type: 'linear'; // 軸のタイプ指定(詳細不明だが，linearでないと全データが等間隔で表示される)
    display: true; // 表示の有無
    position: 'bottom'; // 軸表示位置 top or bottom
    title: {
      display: true; // 軸タイトル表示有無
      align: 'center'; // 軸タイトル表示位置 start or center or end
      text: 'DOY'; // 軸タイトル
      font: {
        size: 14; // 軸タイトルフォントサイズ
      };
    };
    ticks: {
      // stepSize: 0.01, // 軸ラベル間隔
    };
  };
  y: {
    type: 'linear';
    display: true;
    position: 'bottom';
    title: {
      display: true;
      align: 'center';
      text: string;
      font: {
        size: 14;
      };
    };
    ticks: {
      // stepSize: 0.1,
    };
  };
}

/**
 * SDI-12のキーワードから凡例に表示する文言を返す
 *
 * @param dataType SDi-12データタイプ
 */
export const convertSdi12KeyWordToScale = (dataType: string) => {
  switch (dataType) {
    case '':
      return '';
    case 'vwc':
      return '体積含水率(%)';
    case 'brp':
      return 'バルク比誘電率(-)';
    case 'soilTemp':
      return '地温(℃)';
    case 'sbec':
      return 'バルク電気伝導度';
    case 'spwec':
      return '土壌間隙水電気伝導度(μS/cm)';
    case 'gax':
      return '重力加速度(G)';
    case 'gay':
      return '重力加速度(G)';
    case 'gaz':
      return '重力加速度(G)';
  }
};

/**
 * SDI-12のキーワードからグラフタイトルに表示する文言を返す
 *
 * @param dataType SDi-12データタイプ
 */
export const convertSdi12KeyWordToTitle = (dataType: string) => {
  switch (dataType) {
    case '':
      return '表示するデータを選択してください';
    case 'vwc':
      return '体積含水率';
    case 'brp':
      return 'バルク比誘電率';
    case 'soilTemp':
      return '地温';
    case 'sbec':
      return 'バルク電気伝導度';
    case 'spwec':
      return '土壌間隙水電気伝導度';
    case 'gax':
      return '重力加速度';
    case 'gay':
      return '重力加速度';
    case 'gaz':
      return '重力加速度';
  }
};

/**
 * 環境データのキーワードから凡例に表示する文言を返す
 *
 * @param dataType 環境データタイプ
 */
export const convertEnvironmentalKeyWordToScale = (dataType: string) => {
  switch (dataType) {
    case '':
      return '';
    case 'airPress':
      return '大気圧(hPa)';
    case 'temp':
      return '気温(℃)';
    case 'humi':
      return '相対湿度(%)';
    case 'co2Concent':
      return '二酸化炭素濃度(ppm)';
    case 'tvoc':
      return '総揮発性有機化合物量(-)';
    case 'analogValue':
      return 'アナログ値(-)';
  }
};

/**
 * 環境データのキーワードからグラフタイトルに表示する文言を返す
 *
 * @param dataType 環境データタイプ
 */
export const convertEnvironmentalKeyWordToTitle = (dataType: string) => {
  switch (dataType) {
    case '':
      return '表示するデータを選択してください';
    case 'airPress':
      return '大気圧';
    case 'temp':
      return '気温';
    case 'humi':
      return '相対湿度';
    case 'co2Concent':
      return '二酸化炭素濃度';
    case 'tvoc':
      return '総揮発性有機化合物量';
    case 'analogValue':
      return 'アナログ値';
  }
};

/**
 * マイコンIDを指定して測定結果を取得するAPI
 * @return  測定データ or null
 */
export const getMeasuredData = async (microControllerId: string): Promise<MeasuredDataState> => {
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
