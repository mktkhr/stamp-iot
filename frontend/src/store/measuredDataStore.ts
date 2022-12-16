import { getMeasuredData, MeasuredDataState, Sdi12DataState } from '@/type/measuredData';
import { defineStore } from 'pinia';

export const MeasuredDataStore = defineStore('MeasuredDataStore', {
  state: () => ({
    measuredDataList: new MeasuredDataState(),
  }),
  getters: {
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
        this.$state.measuredDataList = measuredDataList;
      }
    },
    getSdi12DataList(dataType: string) {
      const sdi12DatasetList = new Array<datasetFrame>();

      if (this.$state.measuredDataList.sdi12Data != undefined) {
        this.$state.measuredDataList.sdi12Data.forEach(
          (measuredData: Sdi12DataState, index: number) => {
            const sdi12Dataset = new datasetFrame();
            sdi12Dataset.fill = false;
            sdi12Dataset.lineTension = 0;
            sdi12Dataset.borderColor = generateColorStringFromIndex(index);
            sdi12Dataset.pointStyle = 'circle';
            sdi12Dataset.pointRadius = 0;

            let data = new Array<{ x: number; y: number }>();
            if (dataType == 'vwc') {
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
          }
        );
      }

      return sdi12DatasetList;
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
