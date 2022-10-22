<script setup lang="ts">

interface Props {
    title?: string,
    isForMultiMedia?: boolean,
}

withDefaults(defineProps<Props>(), {
    title: '',
    isForMultiMedia: true,
});

</script>

<template>
    <div class="content-box" v-bind:class="{'col': isForMultiMedia, 'col-one-half': isForMultiMedia, 'col-one-fourth': isForMultiMedia,}" >
        <div class="title-box">
            <span>{{title}}</span>
        </div>
        <slot name="content"></slot>
    </div>
</template>

<style scoped>
.content-box {
    width: 100%;
    box-shadow: 0px 10px 20px -5px rgba(0,0,0,0.6);
}
.title-box {
    width: 100%;
    text-align: left;
    height: auto;
    min-height: 40px;
    background-color: #aaa;
    display: table;
}
.title-box span {
    font-size: 18px;
    font-weight: 700;
    padding-left: 15px;
    display: table-cell;
    vertical-align: middle;
}
.col {
    width: calc(100% - 10px);
    margin: 5px;
}

@media (min-width: 600px){
    .col-one-half {
        width: calc(50% - 10px);
    }
    .col {
        float: left;
    }
}

@media (min-width: 1000px){
    .col-one-fourth {
        width: calc(25% - 10px);
    }
}
</style>