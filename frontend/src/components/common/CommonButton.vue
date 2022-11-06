<script setup lang="ts">
	
interface Props {
    buttonTitle?: string,
    errorMessage?: string
}

withDefaults(defineProps<Props>(), {
    buttonTitle: '',
    errorMessage: '',
});

interface Emits {
    (e: "clickButton"): void;
}

const emit = defineEmits<Emits>();

const clickButton = () => {
    emit('clickButton');
}

    
</script>

<template>
    <div>
        <div class="button-wrap">
            <button class="button" @click="clickButton">
                <span class="button-title">{{buttonTitle}}</span>
            </button>
        </div>
        <div class="error-wrap">
            <span v-if="errorMessage != ''" class="error-message">{{ errorMessage }}</span>
        </div>
    </div>
</template>

<style scoped>
.button-wrap {
    width: 100%;
    height: 40px;
    margin: 20px auto;
}
.button {
    height: 100%;
    width: 50%;
    max-width: 200px;
    border-radius: 10px;
    border: none;
    background-color: #aaaaaa;
}
.button:hover {
    background-color: #888888;
}
.button-title {
    font-size: 15px;
    letter-spacing: 2px;
    color: #333333;
}
.error-wrap {
	width: 75%;
	height: 20px;
	margin: 0 auto;
	text-align: center;
}
.error-message {
	color: #EF4868;
	font-size: 12px;
}
@media screen and (max-height: 600px) {
    .button-wrap {
        margin: 10px auto;
    }
}
</style>