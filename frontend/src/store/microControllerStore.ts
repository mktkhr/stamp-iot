import { defineStore } from 'pinia';
import { SpinnerStore } from './spinnerStore';
import {
  MicroController,
  MicroControllerInfoState,
  microControllerDetailGet,
  microControllerDetailPatch,
  MicroControllerDetailPatchParam,
  microControllerGet,
  microControllerRegister,
} from '@/methods/microController';

export const MicroControllerStore = defineStore('MicroControllerStore', {
  state: () => ({
    microControllerList: new Array<MicroControllerInfoState>(),
    microControllerDetail: {} as MicroControllerInfoState,
  }),
  getters: {
    getMicroControllerList: (state): Array<MicroControllerInfoState> => {
      return state.microControllerList;
    },
    getDetail: (state): MicroControllerInfoState => {
      return state.microControllerDetail;
    },
  },
  actions: {
    /**
     * Cookieの情報を基にredisからアカウントUUIDを取得し，アカウントUUIDからマイコンリストをもらってstoreに保存
     */
    async fetchMicroControllerList() {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      try {
        const responseList = await microControllerGet();
        const convertedResponseList = responseList.map((response) => convertToState(response));
        this.$state.microControllerList = convertedResponseList;
      } catch (e) {
        throw e;
      } finally {
        spinnerStore.hideSpinner();
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

    /**
     * マイコン詳細取得
     * @param microControllerUuid マイコンUUID
     */
    async fetchMicroControllerDetail(microControllerUuid: string) {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      try {
        const response = await microControllerDetailGet(microControllerUuid);
        this.$state.microControllerDetail = convertToState(response);
      } catch (e) {
        throw e;
      } finally {
        spinnerStore.hideSpinner();
      }
    },

    /**
     * マイコン詳細更新
     */
    async updateMicroControllerDetail(param: MicroControllerDetailPatchParam) {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      try {
        const response = await microControllerDetailPatch(param);
        this.$state.microControllerDetail = convertToState(response);
      } catch (e) {
        throw e;
      } finally {
        spinnerStore.hideSpinner();
      }
    },
  },
});

/**
 * レスポンスをstateに変換
 * @param microController レスポンス
 */
const convertToState = (microController: MicroController): MicroControllerInfoState => {
  return {
    id: microController.id,
    uuid: microController.uuid,
    name: microController.name,
    macAddress: microController.macAddress,
    interval: microController.interval,
    sdi12Address: microController.sdi12Address,
    createdAt: microController.createdAt,
    updatedAt: microController.updatedAt,
  };
};
