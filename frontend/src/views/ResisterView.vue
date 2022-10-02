<script setup lang="ts">
import HeaderComponent from '@/components/HeaderComponent.vue'
import { ref } from '@vue/reactivity';
import FormWindow from '@/components/common/FormWindow.vue';
import InformationInput from '@/components/common/InformationInput.vue';

const menuStateRef = ref<boolean>();
const changeState = (param: boolean) => {
menuStateRef.value = param;
}
    
</script>

<template>
    <HeaderComponent :hamburgerState=false :menuState="menuStateRef" @clickEvent="changeState"/>
        <FormWindow title="新規登録">
            <template #icon>
                <img src="@/assets/logo_blue.png" alt="logo" />
            </template>
            <template #mailAddress>
                <InformationInput mail-address />
            </template>
            <template #password>
                <InformationInput password />
            </template>
            <template #passwordConfirm>
                <InformationInput passwordConfirm />
            </template>
            <template #link>
                <RouterLink to="/">ログインはこちら</RouterLink>
            </template>
        </FormWindow>
</template>

<style scoped>
img {
    margin-top: 30px;
    height: 50px;
    width: auto;
}
</style>