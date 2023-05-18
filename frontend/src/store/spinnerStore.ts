import { defineStore } from 'pinia';

export const SpinnerStore = defineStore('SpinnerStore', {
  state: () => ({
    status: false,
  }),
  getters: {
    getStatus: (state): boolean => {
      return state.status;
    },
  },
  actions: {
    showSpinner() {
      this.state = true;
    },
    hideSpinner() {
      this.state = false;
    },
  },
});
