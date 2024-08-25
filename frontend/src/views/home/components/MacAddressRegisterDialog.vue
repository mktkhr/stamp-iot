<script setup lang="ts">
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import CommonFormWindow from '@/components/common/commonFormWindow/CommonFormWindow.vue';
import CommonInput from '@/components/common/commonInput/CommonInput.vue';
import CommonOverlay from '@/components/common/commonOverlay/CommonOverlay.vue';
import { i18n } from '@/main';
import validation from '@/methods/validation';
import { ref } from 'vue';

const emit = defineEmits<{
  (e: 'onClickRegister'): void;
}>();
const isShowModal = defineModel<boolean>({ required: true });
const macAddress = defineModel<string>('macAddress', { required: true });
const errorMessage = ref('');

// 初期値
const initMacAddress = ref(macAddress.value);
const initErrorMessage = ref(errorMessage.value);

/**
 * 登録ボタン押下時の処理
 */
const onClickRegister = () => {
  errorMessage.value = '';

  const macAddressValidateFlag = !validation.macAddressValidate(macAddress.value);

  if (macAddressValidateFlag) {
    errorMessage.value = i18n.global.t('Validation.Error.invalidMacAddress');
    return;
  }
  emit('onClickRegister');
};

/**
 * キャンセル押下時の処理
 */
const onClickCancel = () => {
  macAddress.value = initMacAddress.value;
  errorMessage.value = initErrorMessage.value;
  isShowModal.value = false;
};
</script>
<template>
  <CommonOverlay v-model="isShowModal" persistent>
    <CommonFormWindow>
      <template #title>
        <span class="title">{{ $t('Home.registerNewDevice') }}</span>
      </template>
      <template #content>
        <span>{{ $t('Home.enterMacAddress') }}</span>
        <CommonInput
          v-model="macAddress"
          type="text"
          :placeholder="$t('Home.macAddress')"
          :error-message="errorMessage"
        />
        <div class="wrapper-button">
          <CommonButton :button-title="$t('Button.cancel')" @click-button="onClickCancel" />
          <CommonButton
            :button-title="$t('Button.register')"
            @click-button="onClickRegister"
            type="fill"
          />
        </div>
      </template>
    </CommonFormWindow>
  </CommonOverlay>
</template>
<style lang="scss" scoped>
.wrapper {
  &-button {
    display: flex;
    justify-content: space-evenly;
  }
}
</style>
