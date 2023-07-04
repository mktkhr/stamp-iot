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
        class="input-center"
        mail-address
        :error-message="mailAddressError"
        @input-value="getMailAddress"
      />
    </template>
    <template #password>
      <InformationInput
        class="input-center"
        password
        :error-message="passwordError"
        @input-value="getPassword"
      />
    </template>
    <template #passwordConfirm>
      <InformationInput
        class="input-center"
        passwordConfirm
        :error-message="passwordConfirmError"
        @input-value="getPasswordConfirm"
      />
    </template>
    <template #button>
      <div class="wrapper-button">
        <CommonButton
          button-title="登録"
          width="100px"
          height="40px"
          @click-button="onClickRegister"
        />
      </div>
    </template>
    <template #link>
      <div class="wrapper-link">
        <RouterLink to="/login" class="link">ログインはこちら</RouterLink>
      </div>
    </template>
  </FormWindow>
</template>

<style lang="scss" scoped>
.wrapper {
  &-button {
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  &-link {
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
.input-center {
  width: 75%;
  margin: 0 auto 20px;
}
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
