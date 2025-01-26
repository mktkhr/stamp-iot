<script setup lang="ts">
import { generateRandomString } from '@/utils/stringUtil';

interface Props {
  title: string;
}
withDefaults(defineProps<Props>(), {
  title: '',
});

const modelValue = defineModel();
const inputId = generateRandomString();
</script>
<template>
  <div class="wrapper-input">
    <input :id="inputId" type="checkbox" v-model="modelValue" />
    <label :for="inputId">
      {{ title }}
    </label>
  </div>
</template>
<style lang="scss" scoped>
.wrapper {
  &-input {
    display: flex;
    align-items: center;
    pointer-events: all;
  }
}

input[type='checkbox'] {
  display: none;
}

input[type='checkbox'] + label {
  display: block;
  position: relative;
  padding-left: 35px;
  margin-bottom: 20px;
  font: 14px/20px 'Open Sans', Arial, sans-serif;
  color: #444;
  cursor: pointer;
  user-select: none;
}

input[type='checkbox'] + label:last-child {
  margin-bottom: 0;
}

input[type='checkbox'] + label:before {
  content: '';
  display: block;
  width: 20px;
  height: 20px;
  border: 1px solid #444;
  border-radius: 4px;
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0.6;
  -webkit-transition: all 0.12s, border-color 0.08s;
  transition: all 0.12s, border-color 0.08s;
}

input[type='checkbox']:checked + label:before {
  width: 10px;
  top: -5px;
  left: 5px;
  border-radius: 0;
  opacity: 1;
  border: 1px solid var(--ems-theme);
  border-top-color: transparent;
  border-left-color: transparent;
  -webkit-transform: rotate(45deg);
  transform: rotate(45deg);
}
</style>
