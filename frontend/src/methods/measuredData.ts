import { i18n } from '@/main';
import axios from 'axios';

export class MeasuredDataState {
  sdi12Data: Array<Sdi12DataState>;
  environmentalData: Array<EnvironmentalDataState>;
  voltageData: Array<VoltageDataState>;

  constructor(
    sdi12Data: Array<Sdi12DataState>,
    environmentalData: Array<EnvironmentalDataState>,
    voltageData: Array<VoltageDataState>
  ) {
    this.sdi12Data = sdi12Data;
    this.environmentalData = environmentalData;
    this.voltageData = voltageData;
  }
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
  dayOfYear: number;
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

  constructor(
    label: string,
    data: number[],
    fill: boolean | undefined,
    lineTension: number,
    borderColor: string,
    pointStyle: string,
    pointRadius: number
  ) {
    this.label = label;
    this.data = data;
    this.fill = fill;
    this.lineTension = lineTension;
    this.borderColor = borderColor;
    this.pointStyle = pointStyle;
    this.pointRadius = pointRadius;
  }
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
      return i18n.global.t('MeasuredData.volumetricWaterContent') + '(%)';
    case 'brp':
      return i18n.global.t('MeasuredData.bulkRelativePermittivity') + '(-)';
    case 'soilTemp':
      return i18n.global.t('MeasuredData.soilTemperature') + '(℃)';
    case 'sbec':
      return i18n.global.t('MeasuredData.bulkElectricConductivity') + '(μS/cm)';
    case 'spwec':
      return i18n.global.t('MeasuredData.soilPoreWaterElectricConductivity') + '(μS/cm)';
    case 'gax':
      return i18n.global.t('MeasuredData.gravitationalAccelerationX') + '(G)';
    case 'gay':
      return i18n.global.t('MeasuredData.gravitationalAccelerationY') + '(G)';
    case 'gaz':
      return i18n.global.t('MeasuredData.gravitationalAccelerationZ') + '(G)';
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
      return i18n.global.t('MeasuredData.choseData');
    case 'vwc':
      return i18n.global.t('MeasuredData.volumetricWaterContent');
    case 'brp':
      return i18n.global.t('MeasuredData.bulkRelativePermittivity');
    case 'soilTemp':
      return i18n.global.t('MeasuredData.soilTemperature');
    case 'sbec':
      return i18n.global.t('MeasuredData.bulkElectricConductivity');
    case 'spwec':
      return i18n.global.t('MeasuredData.soilPoreWaterElectricConductivity');
    case 'gax':
      return i18n.global.t('MeasuredData.gravitationalAccelerationX');
    case 'gay':
      return i18n.global.t('MeasuredData.gravitationalAccelerationY');
    case 'gaz':
      return i18n.global.t('MeasuredData.gravitationalAccelerationZ');
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
      return i18n.global.t('MeasuredData.airPressure') + '(hPa)';
    case 'temp':
      return i18n.global.t('MeasuredData.temperature') + '(℃)';
    case 'humi':
      return i18n.global.t('MeasuredData.relativeHumidity') + '(%)';
    case 'co2Concent':
      return i18n.global.t('MeasuredData.co2Concentration') + '(ppm)';
    case 'tvoc':
      return i18n.global.t('MeasuredData.totalVolatileOrganicCompounds') + '(-)';
    case 'analogValue':
      return i18n.global.t('MeasuredData.analogValue') + '(-)';
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
      return i18n.global.t('MeasuredData.choseData');
    case 'airPress':
      return i18n.global.t('MeasuredData.airPressure');
    case 'temp':
      return i18n.global.t('MeasuredData.temperature');
    case 'humi':
      return i18n.global.t('MeasuredData.relativeHumidity');
    case 'co2Concent':
      return i18n.global.t('MeasuredData.co2Concentration');
    case 'tvoc':
      return i18n.global.t('MeasuredData.totalVolatileOrganicCompounds');
    case 'analogValue':
      return i18n.global.t('MeasuredData.analogValue');
  }
};

/**
 * マイコンIDを指定して測定結果を取得するAPI
 * @return  測定データ or null
 */
export const fetchMeasuredData = async (
  microControllerUuid: string
): Promise<MeasuredDataState> => {
  const rawResponse = await axios.get('/api/ems/measured-data', {
    params: { microControllerUuid: microControllerUuid },
  });
  return rawResponse.data;
};
