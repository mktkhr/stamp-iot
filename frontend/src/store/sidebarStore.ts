import { defineStore } from 'pinia';

export const SidebarStore = defineStore('SidebarStore', {
  state: () => ({
    status: false,
  }),
  getters: {
    isActive: (state): boolean => {
      return state.status;
    },
  },
  actions: {
    toggleSidebarStatus() {
      this.status = !this.status;
    },
  },
});
