<script setup lang="ts">
import { ref, watch } from 'vue';

interface Props {
  mailAddress?: boolean;
  password?: boolean;
  passwordConfirm?: boolean;
  text?: boolean;
  label?: string;
  errorMessage?: string;
}

withDefaults(defineProps<Props>(), {
  mailAddress: false,
  password: false,
  passwordConfirm: false,
  macAddress: false,
  label: '',
  errorMessage: '',
});

interface Emits {
  (e: 'inputValue', text: string);
}

const emit = defineEmits<Emits>();

const inputValue = ref('');

watch(inputValue, () => {
  emit('inputValue', inputValue.value);
});
</script>

<template>
  <div style="text-align: center">
    <div class="input-wrap">
      <input
        v-if="mailAddress"
        v-bind:class="{ isEditing: inputValue != '' }"
        class="input input-smallest"
        type="email"
        placeholder=""
        v-model="inputValue"
      />
      <input
        v-if="password"
        v-bind:class="{ isEditing: inputValue != '' }"
        class="input input-smallest"
        type="password"
        placeholder=""
        v-model="inputValue"
      />
      <input
        v-if="passwordConfirm"
        v-bind:class="{ isEditing: inputValue != '' }"
        class="input input-smallest"
        type="password"
        placeholder=""
        v-model="inputValue"
      />
      <input
        v-if="text"
        v-bind:class="{ isEditing: inputValue != '' }"
        class="input input-smallest"
        type="text"
        placeholder=""
        v-model="inputValue"
      />
      <label v-if="mailAddress">メールアドレス</label>
      <label v-if="password">パスワード</label>
      <label v-if="passwordConfirm">確認用パスワード</label>
      <label v-if="text">{{ label }}</label>
      <span class="focus_line"><i></i></span>
    </div>
    <div class="error-wrap">
      <p v-if="errorMessage != ''" class="error-message">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<style scoped>
.input-wrap {
  position: relative;
  width: 75%;
  margin: 20px auto 5px;
}
.input-wrap input {
  box-sizing: border-box;
  width: 100%;
  letter-spacing: 1px;
}
.isEditing {
  background: rgba(255, 255, 255, 0.6) !important;
}
.input-wrap input:focus {
  outline: none;
}
.input {
  padding: 7px 14px;
  transition: 0.4s;
  border: 1px solid #1b2538;
  border-radius: 2px;
  background: transparent;
  text-align: center;
}
.input ~ .focus_line:before,
.input ~ .focus_line:after {
  position: absolute;
  top: 0;
  left: 0;
  width: 0;
  height: 2px;
  content: '';
  transition: 0.3s;
  background-color: #2e6dba;
}
.input ~ .focus_line:after {
  top: auto;
  right: 0;
  bottom: 0;
  left: auto;
}
.input ~ .focus_line i:before,
.input ~ .focus_line i:after {
  position: absolute;
  top: 0;
  left: 0;
  width: 2px;
  height: 0;
  content: '';
  transition: 0.4s;
  background-color: #2e6dba;
}
.input ~ .focus_line i:after {
  top: auto;
  right: 0;
  bottom: 0;
  left: auto;
}
.input:focus ~ .focus_line:before,
.input:focus ~ .focus_line:after,
.input-wrap.input ~ .focus_line:before,
.input-wrap.input ~ .focus_line:after {
  width: 100%;
  transition: 0.3s;
}
.input:focus ~ .focus_line i:before,
.input:focus ~ .focus_line i:after,
.input-wrap.input ~ .focus_line i:before,
.input-wrap.input ~ .focus_line i:after {
  height: 100%;
  transition: 0.4s;
}
.input ~ label {
  position: absolute;
  z-index: -1;
  top: 8px;
  left: 0;
  width: 100%;
  transition: 0.3s;
  letter-spacing: 0.5px;
  color: #aaaaaa;
}
.input:focus ~ label,
.input-wrap.input ~ label {
  font-size: 12px;
  top: -18px;
  left: 0;
  transition: 0.3s;
  color: #2e6dba;
}
.error-wrap {
  width: calc(75% - 20px);
  height: 20px;
  padding-left: 20px;
  margin: 0 auto;
  text-align: left;
}
.error-message {
  color: #ef4868;
  font-size: 10px;
  line-height: 10px;
}

@media screen and (max-height: 600px) {
  .input-wrap {
    margin: 5px auto 5px;
  }
  .error-message {
    font-size: 5px;
  }
}
</style>
