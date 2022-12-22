<script setup lang="ts">
import { ref, watch } from 'vue';

interface Props {
  title?: string;
  optionList: { key: string; word: string }[];
  errorMessage?: string;
}

withDefaults(defineProps<Props>(), {
  title: '',
  errorMessage: '',
});

interface Emits {
  (e: 'selectedValue', text: string);
}

const emit = defineEmits<Emits>();

// 初期値
const selectedValue = ref('');

// watchしてemit
watch(selectedValue, () => {
  emit('selectedValue', selectedValue.value);
});
</script>

<template>
  <div>
    <div class="selector-wrapper">
      <select class="selector" v-model="selectedValue" required>
        <option value="" hidden disabled selected></option>
        <option v-for="option in optionList" :key="option.key" :value="option.key">
          {{ option.word }}
        </option>
      </select>
      <span class="highlight"></span>
      <span class="select-bar"></span>
      <label class="select-label">{{ title }}</label>
    </div>
  </div>
</template>

<style scoped>
.selector-wrapper {
  position: relative;
  width: 90%;
  margin: 2em auto;
  text-align: center;
}
.selector {
  position: relative;
  font-family: inherit;
  background-color: transparent;
  width: 100%;
  padding: 10px 10px 10px 0;
  font-size: 18px;
  border-radius: 0;
  border: none;
  border-bottom: 1px solid rgba(0, 0, 0, 0.3);
}
.selector:focus {
  outline: none;
}
.selector-wrapper .selector {
  appearance: none;
  -webkit-appearance: none;
}
.selector-wrapper select::-ms-expand {
  display: none;
}
.selector-wrapper:after {
  position: absolute;
  top: 18px;
  right: 10px;
  width: 0;
  height: 0;
  padding: 0;
  content: '';
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-top: 6px solid rgba(0, 0, 0, 0.3);
  pointer-events: none;
}
.select-label {
  color: rgba(0, 0, 0, 0.5);
  font-size: 16px;
  font-weight: normal;
  position: absolute;
  pointer-events: none;
  left: 0;
  top: 10px;
  transition: 0.2s ease all;
}
.selector:focus ~ .select-label,
.selector:valid ~ .select-label {
  color: #da3c41;
  top: -20px;
  transition: 0.2s ease all;
  font-size: 14px;
}
.select-bar {
  position: relative;
  display: block;
  width: 100%;
}
.select-bar:before,
.select-bar:after {
  content: '';
  height: 2px;
  width: 0;
  bottom: 1px;
  position: absolute;
  background: #da3c41;
  transition: 0.2s ease all;
}
.select-bar:before {
  left: 50%;
}
.select-bar:after {
  right: 50%;
}
.selector:focus ~ .cp_sl06_selectbar:before,
.cp_sl06:focus ~ .cp_sl06_selectbar:after {
  width: 50%;
}
.highlight {
  position: absolute;
  top: 25%;
  left: 0;
  pointer-events: none;
  opacity: 0.5;
}
</style>
