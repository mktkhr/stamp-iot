import { AccountInfoState, getAccountInfo } from '@/type/account/account';
import { defineStore } from 'pinia';

export const AccountStore = defineStore('AccountStore', {
  state: () => ({
    accountId: '',
    name: '',
  }),
  getters: {
    getAccountId: (state): string => {
      return state.accountId;
    },
    getName: (state): string => {
      return state.name;
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
        this.$state.accountId = accountInfo.id;
        this.$state.name = accountInfo.name;
      }
    },
  },
});
