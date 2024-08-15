<script setup lang="ts">
import { AlertStore } from '@/store/alertStore';
import { computed } from 'vue';
import CommonAlert from '../commonAlert/CommonAlert.vue';

const alertStore = AlertStore();
const alertList = computed(() => alertStore.getAlertList);
</script>

<template>
  <div v-if="alertList.length > 0" class="wrapper-alert-box">
    <CommonAlert v-for="alert in alertList" :key="alert.id" :alert="alert" />
  </div>
</template>

<style lang="scss">
.wrapper {
  &-alert-box {
    width: 100%;
    height: auto;
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
}
</style>
