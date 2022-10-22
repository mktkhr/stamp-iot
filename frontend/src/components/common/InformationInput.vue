<script setup lang="ts">
import { ref } from 'vue';
	
interface Props {
    mailAddress?: boolean
    password?: boolean
    passwordConfirm?: boolean
}

withDefaults(defineProps<Props>(), {
    mailAddress: false,
    password: false,
    passwordConfirm: false,
});

const inputValue = ref('');
    
</script>

<template>
    <div class="input-wrap">
        <input v-if="mailAddress" class="input" type="email" placeholder="" v-model="inputValue">
		<input v-if="password" class="input" type="password" placeholder="" v-model="inputValue">
		<input v-if="passwordConfirm" class="input" type="password" placeholder="" v-model="inputValue">
        <label v-if="mailAddress">メールアドレス</label>
		<label v-if="password">パスワード</label>
		<label v-if="passwordConfirm">確認用パスワード</label>
        <span class="focus_line"><i></i></span>
    </div>
</template>

<style scoped>
.input-wrap {
	position: relative;
	width: 80%;
	margin: 40px auto;
}
.input-wrap input {
	font: 15px/24px sans-serif;
	box-sizing: border-box;
	width: 100%;
	letter-spacing: 1px;
}
.input-wrap input:focus {
	outline: none;
}
.input {
	padding: 7px 14px;
	transition: 0.4s;
	border: 1px solid #1b2538;
	border-radius: 5px;
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
	background-color: #2E6DBA;
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
	background-color: #2E6DBA;
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
	color: #2E6DBA;
}

</style>