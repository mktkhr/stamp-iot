<script setup lang="ts">
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import CommonDialog from '@/components/common/commonDialog/CommonDialog.vue';
import CommonOverlay from '@/components/common/commonOverlay/CommonOverlay.vue';

const emit = defineEmits<{
  (e: 'onClickDeny'): void;
  (e: 'onClickAccept'): void;
}>();

const modelValue = defineModel<boolean>({ required: true });
</script>
<template>
  <CommonOverlay v-model="modelValue">
    <CommonDialog
      :title="$t('Dialog.cancelConfirm')"
      :description="$t('Dialog.cancelConfirmDescription')"
    >
      <template #button>
        <CommonButton
          :button-title="$t('Button.no')"
          width="80"
          @click-button="emit('onClickDeny')"
        />
        <CommonButton :button-title="$t('Button.yes')" @click-button="emit('onClickAccept')" />
      </template>
    </CommonDialog>
  </CommonOverlay>
</template>
<style lang="scss" scoped></style>
