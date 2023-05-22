
import { defineStore } from 'pinia';
import { SpinnerStore } from './spinnerStore';
import { MicroControllerInfoState, microControllerGet, microControllerRegister } from '@/methods/microController';

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
    async fetchMicroControllerList() {
      let microControllerList: Array<MicroControllerInfoState>;
      try {
        microControllerList = await microControllerGet();
      } finally {
        this.$state.microControllerList = microControllerList;
      }
    },

    async register(userId: string, macAddress: string) {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      try {
        await microControllerRegister(userId, macAddress);
      } catch (e) {
        throw e;
      } finally {
        spinnerStore.hideSpinner();
      }
    },
  },
});
