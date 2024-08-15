import { defineStore } from 'pinia';

export type AlertContent = {
  id: string;
  type: 'alert' | 'warning' | 'success' | 'info';
  content: string;
  timeInSec?: number | null;
};

export type AlertState = {
  alertMap: Map<string, AlertContent>;
};

export const AlertStore = defineStore('AlertStore', {
  state: (): AlertState => ({
    alertMap: new Map<string, AlertContent>(),
  }),
  getters: {
    /**
     * アラートリストを取得する
     * @returns アラートリスト
     */
    getAlertList: (state): AlertContent[] => {
      return Array.from(state.alertMap.values());
    },
  },
  actions: {
    /**
     * アラートを追加する
     * @param alertContent アラート
     */
    addAlert(alertContent: AlertContent) {
      this.alertMap.set(alertContent.id, alertContent);

      if (!alertContent.timeInSec) return;

      // 時間指定があった場合，時間経過でMapから削除する
      setTimeout(() => {
        this.deleteAlert(alertContent.id);
      }, alertContent.timeInSec * 1000);
    },
    /**
     * 複数のアラートを全て追加する
     * @param alertContentList アラートリスト
     */
    addAlertAll(alertContentList: AlertContent[]) {
      alertContentList.forEach((alertContent: AlertContent) => {
        this.addAlert(alertContent);
      });
    },
    /**
     * アラートを削除する
     * @param key アラートのキー
     */
    deleteAlert(key: string) {
      this.alertMap.delete(key);
    },
    /**
     * アラートを全て削除する
     */
    resetAlert() {
      this.alertMap.clear();
    },
  },
});
