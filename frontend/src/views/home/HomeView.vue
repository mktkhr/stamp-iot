<script setup lang="ts">
import CommonDisplayInformation from '@/components/common/commonDisplayInformation/CommonDisplayInformation.vue';
import DefaultFrame from '@/components/frame/defaultFrame/DefaultFrame.vue';
import GridFrame from '@/components/frame/gridFrame/GridFrame.vue';
import InformationDetailFrame from '@/components/frame/informationDetailFrame/InformationDetailFrame.vue';
import { convertLocalDateTime } from '@/utils/dayjsUtil';
import MacAddressRegisterDialog from './components/MacAddressRegisterDialog.vue';
import { useHome } from './composable';

const {
  accountInfo,
  microControllerList,
  isShowModal,
  macAddressRef,
  onClickPlusButton,
  onClickRegister,
  onClickTile,
  onClickSetting,
} = useHome();
</script>

<template>
  <DefaultFrame :show-action-bar="false">
    <template #content>
      <MacAddressRegisterDialog
        v-model="isShowModal"
        v-model:mac-address="macAddressRef"
        @on-click-register="onClickRegister"
      />

      <div class="wrapper-main-content">
        <div class="wrapper-account-info" v-if="accountInfo">
          <InformationDetailFrame
            class="account-info"
            :title="$t('Home.accountInformation')"
            @clickButton="onClickPlusButton"
          >
            <template #button>
              <span class="ems-add_circle icon" @click="onClickPlusButton"></span>
            </template>
            <template #content>
              <CommonDisplayInformation
                :title="$t('Home.accountName')"
                :content="accountInfo.name ?? $t('Home.unnamed')"
              />
              <CommonDisplayInformation
                :title="$t('Home.registrationDate')"
                :content="convertLocalDateTime(accountInfo.createdAt)"
              />
              <CommonDisplayInformation
                :title="$t('Home.lastUpdatedDate')"
                :content="convertLocalDateTime(accountInfo.updatedAt)"
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
                      <span
                        class="ems-settings icon"
                        @click.stop="onClickSetting(microController.uuid)"
                      ></span>
                    </template>
                    <template #content>
                      <CommonDisplayInformation
                        :title="$t('Home.macAddress')"
                        :content="microController.macAddress"
                      />
                      <CommonDisplayInformation
                        :title="$t('Home.interval')"
                        :content="microController.interval.toString()"
                      />
                      <CommonDisplayInformation
                        :title="$t('Home.registrationDateTime')"
                        :content="convertLocalDateTime(microController.createdAt)"
                      />
                      <CommonDisplayInformation
                        :title="$t('Home.lastUpdatedDateTime')"
                        :content="convertLocalDateTime(microController.updatedAt)"
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
  max-width: 400px;
}
.icon {
  height: 32px;
  width: 32px;
  font-size: 28px;
  color: var(--ems-theme);
  cursor: pointer;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.2s ease;

  &:hover {
    background-color: #00000020;
  }
}
.title {
  font-size: 24px;
}
</style>
