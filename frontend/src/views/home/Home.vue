<script setup lang="ts">
import NotificationBar from '@/components/common/NotificationBar.vue';
import InformationDetailFrame from '@/components/common/InformationDetailFrame.vue';
import DisplayInformation from '@/components/common/DisplayInformation.vue';
import ModalWindow from '@/components/common/ModalWindow.vue';
import InformationInput from '@/components/common/InformationInput.vue';
import CommonButton from '@/components/common/CommonButton.vue';
import DefaultFrame from '@/components/DefaultFrame.vue';
import GridFrame from '@/components/common/GridFrame.vue';

import common from '@/methods/common';
import { useHome } from './composable';

const {
  accountInfo,
  microControllerList,
  isShowModal,
  macAddressError,
  showNotification,
  notificationMessage,
  notificationType,
  onClickPlusButton,
  onClickSubmit,
  getMacAddress,
  onClickRegister,
  onClickTile,
  onClickSetting,
} = useHome();
</script>

<template>
  <NotificationBar :text="notificationMessage" :type="notificationType" v-if="showNotification" />
  <DefaultFrame :show-action-bar="false">
    <template #content>
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

      <div class="wrapper-main-content">
        <div class="wrapper-account-info" v-if="accountInfo">
          <InformationDetailFrame
            class="account-info"
            title="アカウント情報"
            @clickButton="onClickPlusButton"
          >
            <template #button>
              <v-icon class="icon" @click="onClickPlusButton"> ems-add_circle </v-icon>
            </template>
            <template #content>
              <DisplayInformation title="アカウント名" :content="accountInfo.name ?? '未設定'" />
              <DisplayInformation
                title="登録日"
                :content="common.convertLocalDateTime(accountInfo.createdAt)"
              />
              <DisplayInformation
                title="最終更新日"
                :content="common.convertLocalDateTime(accountInfo.updatedAt)"
              />
            </template>
          </InformationDetailFrame>
        </div>
        <div class="wrapper-micro-controller">
          <template v-if="microControllerList">
            <GridFrame>
              <template #content>
                <div
                  class="wrapper-info"
                  v-for="microController in microControllerList"
                  :key="microController.uuid"
                >
                  <InformationDetailFrame
                    class="micro-controller-tile"
                    :title="microController.name ?? '端末名称未設定'"
                    @click="onClickTile(microController.uuid)"
                  >
                    <template #button>
                      <v-icon class="icon" @click.stop="onClickSetting(microController.uuid)">
                        ems-settings
                      </v-icon>
                    </template>
                    <template #content>
                      <DisplayInformation
                        title="MACアドレス"
                        :content="microController.macAddress"
                      />
                      <DisplayInformation
                        title="測定間隔(分)"
                        :content="microController.interval.toString()"
                      />
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
            </GridFrame>
          </template>
        </div>
      </div>
    </template>
  </DefaultFrame>
</template>

<style lang="scss" scoped>
$account_info_height: 150px;
$wrapper_account_info_padding: 10px;
.wrapper {
  &-main-content {
    height: 100%;
    width: 100%;
    overflow-y: auto;
  }
  &-account-info {
    height: #{$account_info_height};
    padding: #{$wrapper_account_info_padding};
    width: calc(100% - #{$wrapper_account_info_padding} * 2); // padding分を引く
  }
  &-micro-controller {
    height: calc(
      100% - #{$account_info_height} - #{$wrapper_account_info_padding} * 2
    ); // padding分を引く
    width: 100%;
    overflow-y: auto;
  }
  &-info {
    padding: 10px;
  }
}
img {
  margin-top: 30px;
  height: 50px;
  width: auto;
}
.micro-controller-tile {
  cursor: pointer;
}
.account-info {
  max-width: 800px;
}
.icon {
  font-size: 30px;
  color: white;
}
</style>
