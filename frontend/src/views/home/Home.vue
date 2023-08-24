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
          :title="$t('Home.registerNewDevice')"
          :description="$t('Home.enterMacAddress')"
        >
          <template #content>
            <div class="wrapper-input">
              <InformationInput
                class="input-content"
                text
                @input-value="getMacAddress"
                :error-message="macAddressError"
              />
            </div>
          </template>
          <template #button>
            <CommonButton :button-title="$t('Button.register')" @click-button="onClickRegister" />
          </template>
        </ModalWindow>
      </v-dialog>

      <div class="wrapper-main-content">
        <div class="wrapper-account-info" v-if="accountInfo">
          <InformationDetailFrame
            class="account-info"
            :title="$t('Home.accountInformation')"
            @clickButton="onClickPlusButton"
          >
            <template #button>
              <v-icon class="icon" @click="onClickPlusButton"> ems-add_circle </v-icon>
            </template>
            <template #content>
              <DisplayInformation
                :title="$t('Home.accountName')"
                :content="accountInfo.name ?? $t('Home.unnamed')"
              />
              <DisplayInformation
                :title="$t('Home.registrationDate')"
                :content="common.convertLocalDateTime(accountInfo.createdAt)"
              />
              <DisplayInformation
                :title="$t('Home.lastUpdatedDate')"
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
                    :title="microController.name ?? $t('Home.unnamedDevice')"
                    @click="onClickTile(microController.uuid)"
                  >
                    <template #button>
                      <v-icon class="icon" @click.stop="onClickSetting(microController.uuid)">
                        ems-settings
                      </v-icon>
                    </template>
                    <template #content>
                      <DisplayInformation
                        :title="$t('Home.macAddress')"
                        :content="microController.macAddress"
                      />
                      <DisplayInformation
                        :title="$t('Home.interval')"
                        :content="microController.interval.toString()"
                      />
                      <DisplayInformation
                        :title="$t('Home.registrationDateTime')"
                        :content="common.convertLocalDateTime(microController.createdAt)"
                      />
                      <DisplayInformation
                        :title="$t('Home.lastUpdatedDateTime')"
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
$account_info_height: 170px;
.wrapper {
  &-main-content {
    height: 100%;
    width: 100%;
    overflow-y: auto;
  }
  &-account-info {
    padding: 10px;
    width: 100%;
  }
  &-micro-controller {
    height: calc(100% - #{$account_info_height});
    width: 100%;
  }
  &-info {
    padding: 10px;
  }
  &-input {
    width: 100%;
    display: flex;
    justify-content: center;
    padding: 8px 16px;
    > .input-content {
      width: 80%;
    }
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
