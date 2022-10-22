<script setup lang="ts">

interface Props {
    title?: string,
    content?: string,
}

const props = withDefaults(defineProps<Props>(), {
    title: '',
    content: '',
});
    
</script>

<template>
    <div class="information-row">
        <div class="information-title">
            <span>{{props.title}}</span>
        </div>
        <div class="information-content">
            <span>{{props.content}}</span>
        </div>
    </div>
</template>

<style scoped>
.information-row {
    display: flex;
    border-top: 1px  solid #33333380;
    border-bottom: 1px  solid #33333380;
}
.information-title {
    width: 40%;
    min-height: 30px;
    height: auto;
    display: table;
    background-color: #eee;
    border-right: 1px solid #33333380;
}
.information-content {
    width: 60%;
    height: 30px;
    display: table;
}
span {
    display: table-cell;
    vertical-align: middle;
}
</style>