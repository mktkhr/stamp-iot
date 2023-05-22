<script setup lang="ts">
import FormWindow from '@/components/common/FormWindow.vue';
import InformationInput from '@/components/common/InformationInput.vue';
import CommonButton from '@/components/common/CommonButton.vue';
import NotificationBar from '@/components/common/NotificationBar.vue';
import { useRegister } from './composable';

const {
  mailAddressError,
  passwordError,
  passwordConfirmError,
  showNotification,
  notificationMessage,
  notificationType,
  getMailAddress,
  getPassword,
  getPasswordConfirm,
  onClickRegister,
} = useRegister();
</script>

<template>
  <NotificationBar :text="notificationMessage" :type="notificationType" v-if="showNotification" />
  <FormWindow title="新規登録">
    <template #icon>
      <img src="@/assets/logo_blue.png" alt="logo" />
    </template>
    <template #mailAddress>
      <InformationInput
        mail-address
        :error-message="mailAddressError"
        @input-value="getMailAddress"
      />
    </template>
    <template #password>
      <InformationInput password :error-message="passwordError" @input-value="getPassword" />
    </template>
    <template #passwordConfirm>
      <InformationInput
        passwordConfirm
        :error-message="passwordConfirmError"
        @input-value="getPasswordConfirm"
      />
    </template>
    <template #button>
      <CommonButton button-title="登録" @click-button="onClickRegister" />
    </template>
    <template #link>
      <RouterLink to="/login" class="link">ログインはこちら</RouterLink>
    </template>
  </FormWindow>
</template>

<style scoped>
img {
  margin-top: 20px;
  height: 50px;
  width: auto;
}
.link {
  background: linear-gradient(transparent 50%, #2e6dba80 50%);
}
@media screen and (max-height: 600px) {
  img {
    margin-top: 5px;
    height: 30px;
  }
}
</style>
