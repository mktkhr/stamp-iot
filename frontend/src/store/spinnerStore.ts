import { defineStore } from 'pinia';

export const SpinnerStore = defineStore('SpinnerStore', {
  state: () => ({
    status: 0,
  }),
  getters: {
    getStatus: (state): boolean => {
      return state.status > 0 ? true : false;
    },
  },
  actions: {
    showSpinner() {
      this.$state.status += 1;
    },
    hideSpinner() {
      this.$state.status -= 1;
    },
  },
});
