<script setup lang="ts">
import HeaderComponent from '@/components/HeaderComponent.vue';
import { computed, ref } from 'vue';
import NavigatorComponent from '@/components/NavigatorComponent.vue';
import InformationDetailFrame from '@/components/common/InformationDetailFrame.vue';
import DisplayInformation from '@/components/common/DisplayInformation.vue';
import ModalWindow from '@/components/common/ModalWindow.vue';
import InformationInput from '@/components/common/InformationInput.vue';
import CommonButton from '@/components/common/CommonButton.vue';

import validation from '@/methods/validation';
import common from '@/methods/common';
import microControllerRegister from '@/methods/microControllerRegister';
import { AccountStore } from '@/store/accountStore';
import { MicroControllerStore } from '@/store/microControllerStore';

// Store
const accountStore = AccountStore();
const microControllerStore = MicroControllerStore();
microControllerStore.fetchAccountInfo();
const microControllerList = computed(() => microControllerStore.getMicroControllerList);

const menuStateRef = ref<boolean>();
const changeState = (param: boolean) => {
  menuStateRef.value = param;
};

const isShowModal = ref<boolean>(false);
const macAddressError = ref<string>('');
const macAddressRef = ref<string>('');

const onClickPlusButton = () => {
  isShowModal.value = true;
};

const onClickSubmit = () => {
  isShowModal.value = false;
};

const getMacAddress = (value: string) => {
  macAddressRef.value = value;
};

const onClickRegister = () => {
  macAddressError.value = '';

  const macAddressValidateFlag = !validation.macAddressValidate(macAddressRef.value);

  if (macAddressValidateFlag) {
    macAddressError.value = 'MACアドレスが正しく入力されていません。';
    return;
  }
  microControllerRegister.post(accountStore.getAccountId, macAddressRef.value);
};
</script>

<template>
  <HeaderComponent hamburgerState :menuState="menuStateRef" @clickEvent="changeState" />
  <NavigatorComponent :menuState="menuStateRef" />
  <v-dialog v-model="isShowModal" width="80%" max-width="500px">
    <ModalWindow
      @click-button="onClickSubmit"
      title="端末新規登録"
      description="登録する端末のMACアドレスを入力して下さい。"
    >
      <template #content>
        <InformationInput text @input-value="getMacAddress" :error-message="macAddressError" />
      </template>
      <template #button>
        <CommonButton button-title="登録" @click-button="onClickRegister" />
      </template>
    </ModalWindow>
  </v-dialog>

  <div class="main-content">
    <v-row class="account-row">
      <InformationDetailFrame
        title="アカウント情報"
        use-account-info
        @clickButton="onClickPlusButton"
      >
        <template #content>
          <DisplayInformation title="アカウント名" :content="accountStore.getName ?? '未設定'" />
          <DisplayInformation title="登録日" content="2022/10/10" />
          <DisplayInformation title="最終更新日" content="2022/10/11" />
        </template>
      </InformationDetailFrame>
    </v-row>
    <!-- FIXME ハードコーディングのため，後でデータに差し替え -->
    <InformationDetailFrame
      v-for="microController in microControllerList"
      :title="microController.name ?? '端末名称未設定'"
      :key="microController.id"
    >
      <template #content>
        <DisplayInformation title="MACアドレス" :content="microController.macAddress" />
        <DisplayInformation title="測定間隔(分)" :content="microController.interval.toString()" />
        <DisplayInformation
          title="登録日時"
          :content="common.convertLocalDateTime(microController.createdAt)"
        />
        <DisplayInformation
          title="更新日時"
          :content="common.convertLocalDateTime(microController.updatedAt)"
        />
      </template>
    </InformationDetailFrame>
  </div>
</template>

<style scoped>
.account-row {
  height: auto;
  margin: 0 0 20px 0;
}
img {
  margin-top: 30px;
  height: 50px;
  width: auto;
}
.main-content {
  padding: 10px;
}
</style>
