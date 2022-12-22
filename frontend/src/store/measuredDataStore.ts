import {
  EnvironmentalDataState,
  getMeasuredData,
  MeasuredDataState,
  Sdi12DataState,
} from '@/type/measuredData';
import { defineStore } from 'pinia';

export const MeasuredDataStore = defineStore('MeasuredDataStore', {
  state: () => ({
    measuredDataList: new MeasuredDataState(),
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
    async fetchMeasuredData(microControllerId: string) {
      let measuredDataList: MeasuredDataState;
      try {
        measuredDataList = await getMeasuredData(microControllerId);
      } finally {
        this.measuredDataList = measuredDataList;
      }
    },

    /**
     * stateに保存されたSDI-12関連データを名称を指定して取得する
     *
     * @param dataType 取得するデータ名
     */
    getSdi12DataList(dataType: string) {
      const sdi12DatasetList = new Array<datasetFrame>();

      if (this.measuredDataList.sdi12Data != undefined) {
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
            sdi12Dataset.label = '体積含水率(アドレス:' + measuredData.sdiAddress + ')';
          } else if (dataType == 'brp') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.brp };
            });
            sdi12Dataset.label = 'バルク比誘電率(アドレス:' + measuredData.sdiAddress + ')';
          } else if (dataType == 'soilTemp') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.soilTemp };
            });
            sdi12Dataset.label = '地温(アドレス:' + measuredData.sdiAddress + ')';
          } else if (dataType == 'sbec') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.sbec };
            });
            sdi12Dataset.label = 'バルク電気伝導度(アドレス:' + measuredData.sdiAddress + ')';
          } else if (dataType == 'spwec') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.spwec };
            });
            sdi12Dataset.label = '土壌間隙水電気伝導度(アドレス:' + measuredData.sdiAddress + ')';
          } else if (dataType == 'gax') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.gax };
            });
            sdi12Dataset.label = '重力加速度(X)(アドレス:' + measuredData.sdiAddress + ')';
          } else if (dataType == 'gay') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.gay };
            });
            sdi12Dataset.label = '重力加速度(Y)(アドレス:' + measuredData.sdiAddress + ')';
          } else if (dataType == 'gaz') {
            data = measuredData.dataList.map((data) => {
              return { x: data.dayOfYear, y: data.gaz };
            });
            sdi12Dataset.label = '重力加速度(Z)(アドレス:' + measuredData.sdiAddress + ')';
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
      const environmentalDatasetList = new Array<datasetFrame>();
      const environmentalDataset = new datasetFrame();
      environmentalDataset.fill = false;
      environmentalDataset.lineTension = 0;
      environmentalDataset.borderColor = generateColorStringFromIndex(0);
      environmentalDataset.pointStyle = 'circle';
      environmentalDataset.pointRadius = 0;

      if (this.measuredDataList.sdi12Data != undefined) {
        environmentalDataset.data = this.measuredDataList.environmentalData.map(
          (data: EnvironmentalDataState) => {
            if (dataType == '') {
              environmentalDataset.label = '';
              return { x: '', y: '' };
            } else if (dataType == 'airPress') {
              environmentalDataset.label = '大気圧';
              return { x: data.dayOfYear, y: data.airPress };
            } else if (dataType == 'temp') {
              environmentalDataset.label = '気温';
              return { x: data.dayOfYear, y: data.temp };
            } else if (dataType == 'humi') {
              environmentalDataset.label = '相対湿度';
              return { x: data.dayOfYear, y: data.humi };
            } else if (dataType == 'co2Concent') {
              environmentalDataset.label = '二酸化炭素濃度';
              return { x: data.dayOfYear, y: data.co2Concent };
            } else if (dataType == 'tvoc') {
              environmentalDataset.label = '総揮発性有機化合物量';
              return { x: data.dayOfYear, y: data.tvoc };
            } else if (dataType == 'analogValue') {
              environmentalDataset.label = 'アナログ値';
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
  label: string;
  data: Array<{ x: number; y: number }>;
  fill?: boolean;
  lineTension: number;
  borderColor: string;
  pointStyle: string;
  pointRadius: number;
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
  }
};
