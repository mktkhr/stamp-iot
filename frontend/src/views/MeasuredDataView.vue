<script setup lang="ts">
import HeaderComponent from '@/components/HeaderComponent.vue';
import { computed, ref, watch } from 'vue';
import NavigatorComponent from '@/components/NavigatorComponent.vue';
import { MicroControllerStore } from '@/store/microControllerStore';
import { MeasuredDataStore } from '@/store/measuredDataStore';
import { useRoute } from 'vue-router';
import { LineChart } from 'vue-chart-3';
import { Chart, ChartData, ChartOptions, registerables } from 'chart.js';

// Route
const route = useRoute();
const microControllerId = route.params.microControllerId[0];

// Store
const measuredDataStore = MeasuredDataStore();
measuredDataStore.fetchMeasuredData(microControllerId);
const microControllerStore = MicroControllerStore();
microControllerStore.fetchAccountInfo();

// ナビゲーター関連
const menuStateRef = ref<boolean>();
const changeState = (param: boolean) => {
  menuStateRef.value = param;
};

// Chart関連
Chart.register(...registerables);

const config = computed<ChartOptions<'line'>>(() => ({
  responsive: true,
  scales: {
    x: {
      // 軸設定(https://www.chartjs.org/docs/latest/axes/)
      type: 'linear', // 軸のタイプ指定(詳細不明だが，linearでないと全データが等間隔で表示される)
      display: true, // 表示の有無
      position: 'bottom', // 軸表示位置 top or bottom
      title: {
        display: true, // 軸タイトル表示有無
        align: 'center', // 軸タイトル表示位置 start or center or end
        text: 'DOY', // 軸タイトル
        font: {
          size: 14, // 軸タイトルフォントサイズ
        },
      },
      ticks: {
        // stepSize: 0.01, // 軸ラベル間隔
      },
    },
    y: {
      type: 'linear',
      display: true,
      position: 'bottom',
      title: {
        display: true,
        align: 'center',
        text: 'Y軸タイトル',
        font: {
          size: 14,
        },
      },
      ticks: {
        // stepSize: 0.1,
      },
    },
  },
  plugins: {
    legend: {
      display: true, // 凡例表示有無
    },
    title: {
      text: 'グラフタイトル', // グラフタイトル
      display: true, // グラフタイトル表示有無
      font: {
        size: 16, // グラフタイトルフォントサイズ
      },
    },
  },
}));

const dataset = computed(() => measuredDataStore.getSdi12DataList('soilTemp')); // ここをセレクト要素で分岐させる
const lineData = computed<ChartData<'line'>>(() => ({
  datasets: dataset.value,
}));
</script>

<template>
  <HeaderComponent hamburgerState :menuState="menuStateRef" @clickEvent="changeState" />
  <NavigatorComponent :menuState="menuStateRef" />
  <div class="main-content">
    <div class="graph-wrapper">
      <LineChart ref="lineRef" :chartData="lineData" :options="config" />
    </div>
  </div>
</template>

<style scoped>
.main-content {
  padding: 10px;
}
</style>
