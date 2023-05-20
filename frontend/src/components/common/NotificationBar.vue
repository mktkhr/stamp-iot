<script setup lang="ts">
import { NotificationType } from '@/constants/notificationType';

interface Props {
  text: string;
  type: number;
  closable?: boolean;
  height?: number;
}

withDefaults(defineProps<Props>(), {
  text: '',
  type: 1,
  closable: true,
  height: 50,
});
</script>

<template>
  <v-alert
    v-if="type === NotificationType.INFO"
    :text="text"
    :closable="closable"
    type="info"
    :height="height"
  />
  <v-alert
    v-if="type === NotificationType.SUCCESS"
    :text="text"
    :closable="closable"
    type="success"
    :height="height"
  />
  <v-alert
    v-if="type === NotificationType.WARNING"
    :text="text"
    :closable="closable"
    type="warning"
    :height="height"
  />
  <v-alert
    v-if="type === NotificationType.ERROR"
    :text="text"
    :closable="closable"
    type="error"
    :height="height"
  />
</template>

<style lang="scss" scoped>
// density="compact" で高さが十分に小さくならないため，vuetify のデフォルト値を上書き
.v-alert {
  padding: 0 10px;
  border-radius: 0 0 2px 2px;
}
</style>
