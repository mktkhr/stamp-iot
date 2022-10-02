<script setup lang="ts">
    
</script>

<template>
    <div class="form-frame">
        <slot name="icon" class="icon"></slot>
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
        max-height: 800px;
        border: 1px solid #888888;
        border-radius: 5px;
        margin: calc((40vh - 50px) / 2) auto;
        box-shadow: 0px 10px 20px -5px rgba(0,0,0,0.6);
    }
    .icon img{
        height: 10px;
        width: auto;
    }

</style>