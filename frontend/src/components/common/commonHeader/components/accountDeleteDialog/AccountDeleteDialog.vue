<script setup lang="ts">
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import CommonCheckBox from '@/components/common/commonCheckBox/CommonCheckBox.vue';
import CommonDialog from '@/components/common/commonDialog/CommonDialog.vue';
import { ref } from 'vue';

const emit = defineEmits<{
  (e: 'onClickBack'): void;
  (e: 'onClickDeleteAccept'): void;
}>();

const isDeleteAccountChecked = ref(false);
const isDeleteAccountDataChecked = ref(false);
</script>
<template>
  <CommonDialog
    :title="$t('Dialog.accountDeleteConfirm')"
    :description="$t('Dialog.accountDeleteConfirmCheck')"
  >
    <template #button>
      <CommonButton
        :button-title="$t('Button.cancel')"
        width="auto"
        @click-button="emit('onClickBack')"
      />
      <CommonButton
        v-if="isDeleteAccountChecked && isDeleteAccountDataChecked"
        class="delete-button"
        :button-title="$t('Button.delete')"
        width="auto"
        background-color="var(--ems-alert-theme)"
        title-color="white"
        @click-button="emit('onClickDeleteAccept')"
      />
    </template>

    <CommonCheckBox
      v-model="isDeleteAccountChecked"
      :title="$t('Dialog.accountDeleteConfirmDescription')"
    />
    <CommonCheckBox
      v-model="isDeleteAccountDataChecked"
      :title="$t('Dialog.relatedDataDeleteConfirmDescription')"
    />
  </CommonDialog>
</template>
<style lang="scss" scoped></style>
