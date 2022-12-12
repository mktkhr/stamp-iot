import { getMeasuredData, MeasuredDataState } from '@/type/measuredData';
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
  },
});
