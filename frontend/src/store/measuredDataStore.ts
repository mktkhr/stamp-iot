import {
  EnvironmentalDataState,
  fetchMeasuredData,
  MeasuredDataState,
  Sdi12DataState,
} from '@/methods/measuredData';
import { defineStore } from 'pinia';
import { SpinnerStore } from './spinnerStore';
import { i18n } from '@/main';

export const MeasuredDataStore = defineStore('MeasuredDataStore', {
  state: () => ({
    measuredDataList: new MeasuredDataState([], [], []),
  }),

  getters: {
    /**
     * 測定データを取得する
     */
    getMeasuredDataList: (state): MeasuredDataState => {
      return state.measuredDataList;
    },
  },
  actions: {
    /**
     * Cookieの情報を基にredisからアカウントUUIDを取得し，アカウントUUIDからマイコンリストをもらってstoreに保存
     */
    async fetchMeasuredData(microControllerUuid: string) {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      let measuredDataList: MeasuredDataState;
      try {
        measuredDataList = await fetchMeasuredData(microControllerUuid);
        this.measuredDataList = measuredDataList;
      } finally {
        spinnerStore.hideSpinner();
      }
    },

    /**
     * stateに保存されたSDI-12関連データを名称を指定して取得する
     *
     * @param dataType 取得するデータ名
     */
    getSdi12DataList(dataType: string) {
      const measuredDataStore = MeasuredDataStore();
      const sdi12DatasetList = new Array<datasetFrame>();

      if (
        measuredDataStore.getMeasuredDataList &&
        measuredDataStore.getMeasuredDataList.sdi12Data &&
        measuredDataStore.getMeasuredDataList.sdi12Data.length > 0
      ) {
        this.measuredDataList.sdi12Data.forEach((measuredData: Sdi12DataState, index: number) => {
          const sdi12Dataset = new datasetFrame();
          sdi12Dataset.fill = false;
          sdi12Dataset.lineTension = 0;
          sdi12Dataset.borderColor = generateColorStringFromIndex(index);
          sdi12Dataset.pointStyle = 'circle';
          sdi12Dataset.pointRadius = 0;

          let data = new Array<{ x: number; y: number }>();
          if (dataType == '') {
            data = [];
            sdi12Dataset.label = '';
          } else if (dataType == 'vwc') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.vwc };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.volumetricWaterContent') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          } else if (dataType == 'brp') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.brp };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.bulkRelativePermittivity') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          } else if (dataType == 'soilTemp') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.soilTemp };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.temperature') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          } else if (dataType == 'sbec') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.sbec };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.bulkElectricConductivity') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          } else if (dataType == 'spwec') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.spwec };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.soilPoreWaterElectricConductivity') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          } else if (dataType == 'gax') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.gax };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.gravitationalAccelerationX') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          } else if (dataType == 'gay') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.gay };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.gravitationalAccelerationY') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          } else if (dataType == 'gaz') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.gaz };
            });
            sdi12Dataset.label =
              i18n.global.t('MeasuredData.gravitationalAccelerationZ') +
              '(アドレス:' +
              measuredData.sdiAddress +
              ')';
          }

          sdi12Dataset.data = data;
          sdi12DatasetList.push(sdi12Dataset);
        });
      }

      return sdi12DatasetList;
    },

    /**
     * stateに保存された環境関連データを名称を指定して取得する
     *
     * @param dataType 取得するデータ名
     */
    getEnvironmentDataList(dataType: string) {
      const measuredDataStore = MeasuredDataStore();
      const environmentalDatasetList = new Array<datasetFrame>();
      const environmentalDataset = new datasetFrame();
      environmentalDataset.fill = false;
      environmentalDataset.lineTension = 0;
      environmentalDataset.borderColor = generateColorStringFromIndex(0);
      environmentalDataset.pointStyle = 'circle';
      environmentalDataset.pointRadius = 0;

      if (
        measuredDataStore.getMeasuredDataList &&
        measuredDataStore.getMeasuredDataList.environmentalData &&
        measuredDataStore.getMeasuredDataList.environmentalData.length > 0
      ) {
        environmentalDataset.data = this.measuredDataList.environmentalData.map(
          (data: EnvironmentalDataState) => {
            if (dataType === '') {
              environmentalDataset.label = '';
              return { x: '', y: '' };
            } else if (dataType === 'airPress') {
              environmentalDataset.label = i18n.global.t('MeasuredData.airPressure');
              return { x: data.dayOfYear, y: data.airPress };
            } else if (dataType === 'temp') {
              environmentalDataset.label = i18n.global.t('MeasuredData.temperature');
              return { x: data.dayOfYear, y: data.temp };
            } else if (dataType === 'humi') {
              environmentalDataset.label = i18n.global.t('MeasuredData.relativeHumidity');
              return { x: data.dayOfYear, y: data.humi };
            } else if (dataType === 'co2Concent') {
              environmentalDataset.label = i18n.global.t('MeasuredData.co2Concentration');
              return { x: data.dayOfYear, y: data.co2Concent };
            } else if (dataType === 'tvoc') {
              environmentalDataset.label = i18n.global.t(
                'MeasuredData.totalVolatileOrganicCompounds'
              );
              return { x: data.dayOfYear, y: data.tvoc };
            } else if (dataType === 'analogValue') {
              environmentalDataset.label = i18n.global.t('MeasuredData.analogValue');
              return { x: data.dayOfYear, y: data.analogValue };
            }
          }
        );
      }

      environmentalDatasetList.push(environmentalDataset);
      return environmentalDatasetList;
    },
  },
});

class datasetFrame {
  label: string = '';
  data: Array<{ x: number; y: number }> = [];
  fill?: boolean;
  lineTension: number = 0;
  borderColor: string = 'rgba(0, 0, 0, 0.5)';
  pointStyle: string = '';
  pointRadius: number = 0;
}

/**
 * indexから色を生成する(store内使用)
 *
 * @param index index番号
 */
const generateColorStringFromIndex = (index: number) => {
  switch (index) {
    case 0:
      return 'rgba(0, 0, 0, 0.5)';
    case 1:
      return 'rgba(255, 0, 0, 0.5)';
    case 2:
      return 'rgba(0, 0, 255, 0.5)';
    case 3:
      return 'rgba(0, 255, 0, 0.5)';
    case 4:
      return 'rgba(255, 255, 0, 0.5)';
    case 5:
      return 'rgba(255, 0, 255, 0.5)';
    case 6:
      return 'rgba(0, 255, 255, 0.5)';
    default:
      return 'rgba(0, 0, 0, 0.5)';
  }
};
