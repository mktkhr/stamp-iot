import {
  AccountInfoState,
  deleteAccount,
  getAccountInfo,
  login,
  logout,
  register,
} from '@/methods/account';
import { defineStore } from 'pinia';
import { SpinnerStore } from './spinnerStore';

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
      const accountInfo = await getAccountInfo();
      this.$state.account = accountInfo;
    },
    async login(mailAddress: string, password: string) {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      try {
        await login(mailAddress, password);
      } finally {
        spinnerStore.hideSpinner();
      }
    },
    async logout() {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();
      try {
        await logout();
      } finally {
        spinnerStore.hideSpinner();
      }
    },
    async register(mailAddress: string, password: string) {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      try {
        await register(mailAddress, password);
      } finally {
        spinnerStore.hideSpinner();
      }
    },
    async delete() {
      const spinnerStore = SpinnerStore();
      spinnerStore.showSpinner();

      try {
        await deleteAccount();
      } finally {
        spinnerStore.hideSpinner();
      }
    },
  },
});
