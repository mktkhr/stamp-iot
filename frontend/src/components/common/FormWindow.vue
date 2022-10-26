<script setup lang="ts">

const props = defineProps<{
    title?: string
}>()

</script>

<template>
    <div class="form-frame">
        <slot name="icon" class="icon"></slot>
        <div class="title">
            <span v-if="title">{{props.title}}</span>
        </div>
        <slot name="mailAddress"></slot>
        <slot name="password"></slot>
        <slot name="passwordConfirm"></slot>
        <div>
            <slot name="link"></slot>
        </div>
    </div>
</template>

<style scoped>
.form-frame {
    width: 80%;
    max-width: 600px;
    height: 60vh;
    height: 60dvh;
    max-height: 800px;
    border: 1px solid #888888;
    border-radius: 5px;
    margin: calc((30vh - 50px) / 2) auto;
    margin: calc((30dvh - 50px) / 2) auto;
    box-shadow: 0px 10px 20px -5px rgba(0,0,0,0.6);
}
.title {
    margin-bottom: 10px;
}
.title span {
    font-size: 25px;
}

@media screen and (max-height: 900px) {
    .form-frame {
        margin: calc((20vh - 50px) / 2) auto;
        margin: calc((20dvh - 50px) / 2) auto;
        height: 80vh;
        height: 80dvh;
    }
    .title span {
        font-size: 20px;
    }
}

</style>