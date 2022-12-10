import { AccountInfoState, getAccountInfo } from '@/type/account';
import { defineStore } from 'pinia';

export const AccountStore = defineStore('AccountStore', {
  state: () => ({
    account: new AccountInfoState(),
  }),
  getters: {
    getAccountInfo: (state): AccountInfoState => {
      return state.account;
    },
  },
  actions: {
    /**
     * Cookieの情報を基にredisからアカウントUUIDを取得し，アカウントUUIDからアカウント情報をもらってstoreに保存
     */
    async fetchAccountInfo() {
      let accountInfo: AccountInfoState;
      try {
        accountInfo = await getAccountInfo();
      } finally {
        this.$state.account = accountInfo;
      }
    },
  },
});
