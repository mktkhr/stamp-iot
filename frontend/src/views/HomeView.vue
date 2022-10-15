<script setup lang="ts">
import HeaderComponent from '@/components/HeaderComponent.vue'
import { ref } from 'vue';
import NavigatorComponent from '@/components/NavigatorComponent.vue';
import InformationDetailFrame from '@/components/common/InformationDetailFrame.vue';
import DisplayInformation from '@/components/common/DisplayInformation.vue'
  
  const menuStateRef = ref<boolean>();
  const changeState = (param: boolean) => {
      menuStateRef.value = param;
  }
</script>

<template>
  <HeaderComponent hamburgerState :menuState="menuStateRef" @clickEvent="changeState" />
  <NavigatorComponent :menuState="menuStateRef"></NavigatorComponent>
  <div class="main-content">
    <!-- FIXME ハードコーディングのため，後でデータに差し替え -->
    <InformationDetailFrame title="アカウント情報">
      <template #content >
        <DisplayInformation title="アカウント名" content="sample" />
        <DisplayInformation title="登録日" content="2022/10/10" />
        <DisplayInformation title="最終更新日" content="2022/10/11" />
      </template>
    </InformationDetailFrame>
    <InformationDetailFrame title="登録端末1">
      <template #content >
        <DisplayInformation title="シリアル番号" content="sn_sample" />
        <DisplayInformation title="最終更新時刻" content="2022/10/11 10:10:10" />
      </template>
    </InformationDetailFrame>
  </div>
</template>

<style scoped>
img {
  margin-top: 30px;
  height: 50px;
  width: auto;
}
.main-content {
  padding: 10px;
}
</style>