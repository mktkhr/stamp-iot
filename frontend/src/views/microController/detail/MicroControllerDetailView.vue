<script setup lang="ts">
import DefaultFrame from '@/components/DefaultFrame.vue';
import DisplayInformation from '@/components/common/DisplayInformation.vue';
import InformationInput from '@/components/common/InformationInput.vue';
import InformationSelect from '@/components/common/InformationSelect.vue';
import ModalWindow from '@/components/common/ModalWindow.vue';
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import { i18n } from '@/main';
import { MicroControllerDetailPatchParam } from '@/methods/microController';
import validation from '@/methods/validation';
import { MicroControllerStore } from '@/store/microControllerStore';
import dayjs from 'dayjs';
import { computed, ref } from 'vue';

const props = defineProps<{
  microControllerUuid: string;
}>();

const isEditMode = ref(false);
const intervalForEdit = ref('');
const unitNameForEdit = ref('');
const addressForEdit = ref('');
const isShowModal = ref(false);
const sdiAddressErrorMessage = ref('');

const microControllerStore = MicroControllerStore();
const microControllerDetail = computed(() => microControllerStore.getDetail);

const fetchDetail = async () => {
  await microControllerStore.fetchMicroControllerDetail(props.microControllerUuid);
};
fetchDetail();

const intervalList = [
  { key: '1', word: '1分' },
  { key: '5', word: '5分' },
  { key: '10', word: '10分' },
  { key: '15', word: '15分' },
  { key: '20', word: '20分' },
  { key: '30', word: '30分' },
  { key: '60', word: '60分' },
];

const onClickEdit = () => {
  intervalForEdit.value = microControllerDetail.value.interval.toString();
  unitNameForEdit.value = microControllerDetail.value.name ?? '';
  addressForEdit.value = microControllerDetail.value.sdi12Address;
  isEditMode.value = true;
};
const onClickCancel = () => {
  if (checkDiff()) {
    isShowModal.value = true;
    return;
  }
  sdiAddressErrorMessage.value = '';
  isEditMode.value = false;
};
const onClickSave = () => {
  sdiAddressErrorMessage.value = '';
  if (!validation.sdiAddressValidate(addressForEdit.value)) {
    sdiAddressErrorMessage.value = i18n.global.t('Validation.invalidSdiAddress');
    return;
  }

  const param: MicroControllerDetailPatchParam = {
    microControllerUuid: props.microControllerUuid,
    name: unitNameForEdit.value,
    interval: intervalForEdit.value,
    sdi12Address: addressForEdit.value,
  };

  microControllerStore.updateMicroControllerDetail(param);
  isEditMode.value = false;
};

const checkDiff = () => {
  return (
    microControllerDetail.value.interval.toString() !== intervalForEdit.value ||
    microControllerDetail.value.name !== unitNameForEdit.value
  );
};

const onInputName = (value: string) => {
  unitNameForEdit.value = value;
};

const onInputAddress = (value: string) => {
  addressForEdit.value = value;
};

const onClickAccept = () => {
  isEditMode.value = false;
  isShowModal.value = false;
};

const onClickDeny = () => {
  isShowModal.value = false;
};

const onChangeSelectedValue = (value: string) => {
  intervalForEdit.value = value;
};
</script>
<template>
  <DefaultFrame show-action-bar>
    <template #actionBar>
      <div class="action-bar">
        <div class="wrapper-button">
          <CommonButton
            v-if="!isEditMode"
            :button-title="$t('Button.edit')"
            @clickButton="onClickEdit"
          />
          <CommonButton
            v-if="isEditMode"
            :button-title="$t('Button.cancel')"
            width="120px"
            @clickButton="onClickCancel"
          />
          <CommonButton
            v-if="isEditMode"
            :button-title="$t('Button.save')"
            @clickButton="onClickSave"
          />
        </div>
      </div>
    </template>
    <template #content>
      <div class="wrapper-content">
        <div class="wrapper-unit-info">
          <div class="wrapper-unit-photo">
            <v-icon class="icon">ems-photo</v-icon>
          </div>
          <div class="wrapper-unit-name">
            <div class="span-name-box">
              <span v-if="!isEditMode" class="name">{{
                `${microControllerDetail.name ?? $t('microControllerDetail.unnamedDevice')}`
              }}</span>
              <InformationInput
                v-if="isEditMode"
                text
                align-left
                :init-value="unitNameForEdit"
                @inputValue="onInputName"
              />
              <span class="address">{{
                `${$t('microControllerDetail.macAddress')}: ${microControllerDetail.macAddress}`
              }}</span>
            </div>
          </div>
        </div>
        <DisplayInformation
          :title="$t('microControllerDetail.interval')"
          :content="`${microControllerDetail.interval}分`"
          show-border-top
          :is-edit-mode="isEditMode"
        >
          <template #content>
            <InformationSelect
              v-if="isEditMode"
              class="selector"
              :option-list="intervalList"
              :title="$t('microControllerDetail.interval')"
              :show-label="false"
              :init-value="intervalForEdit"
              outlined
              @selected-value="onChangeSelectedValue"
            />
          </template>
        </DisplayInformation>
        <DisplayInformation
          :title="$t('microControllerDetail.measurementAddress')"
          :content="
            microControllerDetail.sdi12Address ?? $t('microControllerDetail.unsetSdiAddress')
          "
          :is-edit-mode="isEditMode"
        >
          <template #content>
            <InformationInput
              class="address-input"
              v-if="isEditMode"
              text
              align-left
              :init-value="addressForEdit"
              :error-message="sdiAddressErrorMessage"
              @inputValue="onInputAddress"
            />
          </template>
        </DisplayInformation>
        <DisplayInformation
          :title="$t('microControllerDetail.registrationDate')"
          :content="`${dayjs(microControllerDetail.createdAt).format('YYYY/MM/DD')}`"
        />
        <DisplayInformation
          :title="$t('microControllerDetail.lastUpdatedDate')"
          :content="`${dayjs(microControllerDetail.updatedAt).format('YYYY/MM/DD')}`"
        />

        <v-dialog v-model="isShowModal" width="80%" max-width="500px" height="200px">
          <ModalWindow
            :title="$t('Dialog.cancelConfirm')"
            :description="$t('Dialog.cancelConfirmDescription')"
          >
            <template #button>
              <CommonButton
                :button-title="$t('Button.no')"
                width="80"
                @click-button="onClickDeny"
              />
              <CommonButton :button-title="$t('Button.yes')" @click-button="onClickAccept" />
            </template>
          </ModalWindow>
        </v-dialog>
      </div>
    </template>
  </DefaultFrame>
</template>

<style lang="scss" scoped>
$unit_info_height: 100px;
.wrapper {
  &-content {
    height: 100%;
    width: 100%;
    overflow-y: auto;
  }
  &-unit-info {
    height: #{$unit_info_height};
    width: 100%;
    display: flex;
  }
  &-unit-photo {
    height: 100%;
    width: #{$unit_info_height};
    display: flex;
    justify-content: center;
    align-items: center;
    > .icon {
      border: 2px solid #00000022;
      border-radius: 50%;
      height: 80px;
      width: 80px;
      font-size: 50px;
    }
  }
  &-unit-name {
    height: 100%;
    width: calc(100% - #{$unit_info_height});
    position: relative;
    display: flex;
    align-items: center;
    padding-left: 20px;
  }
}
.span {
  &-name-box {
    display: flex;
    flex-direction: column;
    text-align: left;
    > .name {
      font-size: 18px;
      max-width: 200px;
    }
    > .address {
      font-size: 12px;
      color: #888888;
    }
  }
}
.action-bar {
  width: 100%;
  height: 100%;
  border-bottom: 1px solid #00000022;
  > .wrapper-button {
    position: absolute;
    right: 0;
    top: 0;
    padding: 10px;
    display: flex;
    gap: 20px;
  }
}

.selector {
  width: calc(100% - 20px);
  height: calc(100% - 20px);
}
.address-input {
  width: calc(100% - 20px);
  height: calc(100% - 20px);
  padding: 10px;
}
</style>
