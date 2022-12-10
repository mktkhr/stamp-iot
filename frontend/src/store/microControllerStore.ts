import { getMicroControllerInfo, MicroControllerInfoState } from '@/type/microController';
import { defineStore } from 'pinia';

export const MicroControllerStore = defineStore('MicroControllerStore', {
  state: () => ({
    microControllerList: new Array<MicroControllerInfoState>(),
  }),
  getters: {
    getMicroControllerList: (state): Array<MicroControllerInfoState> => {
      return state.microControllerList;
    },
  },
  actions: {
    /**
     * Cookieの情報を基にredisからアカウントUUIDを取得し，アカウントUUIDからマイコンリストをもらってstoreに保存
     */
    async fetchAccountInfo() {
      let microControllerList: Array<MicroControllerInfoState>;
      try {
        microControllerList = await getMicroControllerInfo();
      } finally {
        this.$state.microControllerList = microControllerList;
      }
    },
  },
});
