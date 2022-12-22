<script setup lang="ts">
import HeaderComponent from '@/components/HeaderComponent.vue';
import { computed, ref, watch } from 'vue';
import NavigatorComponent from '@/components/NavigatorComponent.vue';
import InformationSelect from '@/components/common/InformationSelect.vue';
import { MicroControllerStore } from '@/store/microControllerStore';
import { MeasuredDataStore } from '@/store/measuredDataStore';
import { useRoute } from 'vue-router';
import { LineChart } from 'vue-chart-3';
import { Chart, ChartData, ChartOptions, registerables } from 'chart.js';
import {
  convertSdi12KeyWordToTitle,
  convertSdi12KeyWordToScale,
  convertEnvironmentalKeyWordToTitle,
  convertEnvironmentalKeyWordToScale,
} from '@/type/measuredData';

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

// 初期値
const selectedSdi12Option = ref('');
const selectedEnvironmentalOption = ref('');

// Chart関連
Chart.register(...registerables);


// SDI-12グラフの設定
const sdi12ChartConfig = computed<ChartOptions<'line'>>(() => ({
  responsive: true,
  scales: {
    x: {
      type: 'linear',
      display: true,
      position: 'bottom',
      title: {
        display: true,
        align: 'center',
        text: 'DOY',
        font: {
          size: 14,
        },
      },
    },
    y: {
      type: 'linear',
      display: true,
      position: 'bottom',
      title: {
        display: true,
        align: 'center',
        text: convertSdi12KeyWordToScale(selectedSdi12Option.value),
        font: {
          size: 14,
        },
      },
    },
  },
  plugins: {
    legend: {
      display: true,
    },
    title: {
      text: convertSdi12KeyWordToTitle(selectedSdi12Option.value),
      display: true,
      font: {
        size: 16,
      },
    },
  },
}));

// 環境データグラフの設定
const environmentalChartConfig = computed<ChartOptions<'line'>>(() => ({
  responsive: true,
  scales: {
    x: {
      type: 'linear',
      display: true,
      position: 'bottom',
      title: {
        display: true,
        align: 'center',
        text: 'DOY',
        font: {
          size: 14,
        },
      },
    },
    y: {
      type: 'linear',
      display: true,
      position: 'bottom',
      title: {
        display: true,
        align: 'center',
        text: convertEnvironmentalKeyWordToScale(selectedEnvironmentalOption.value),
        font: {
          size: 14,
        },
      },
    },
  },
  plugins: {
    legend: {
      display: true,
    },
    title: {
      text: convertEnvironmentalKeyWordToTitle(selectedEnvironmentalOption.value),
      display: true,
      font: {
        size: 16,
      },
    },
  },
}));

// SDI-12グラフ表示切替用選択肢
const sdi12OptionList = [
  { word: '体積含水率', key: 'vwc' },
  { word: 'バルク比誘電率', key: 'brp' },
  { word: '地温', key: 'soilTemp' },
  { word: 'バルク電気伝導度', key: 'sbec' },
  { word: '土壌間隙水電気伝導度', key: 'spwec' },
  { word: '重力加速度(X)', key: 'gax' },
  { word: '重力加速度(Y)', key: 'gay' },
  { word: '重力加速度(Z)', key: 'gaz' },
];

// 環境データグラフ表示切替用選択肢
const environmentalOptionList = [
  { word: '大気圧', key: 'airPress' },
  { word: '気温', key: 'temp' },
  { word: '相対湿度', key: 'humi' },
  { word: '二酸化炭素濃度', key: 'co2Concent' },
  { word: '総揮発性有機化合物量', key: 'tvoc' },
  { word: 'アナログ値', key: 'analogValue' },
];

/**
 * SDI-12の表示項目セレクターが変化した際の処理
 * @param value
 */
const onChangeSdi12Select = (value: string) => {
  selectedSdi12Option.value = value;
};

// SDI-12グラフ用データ
const sdi12ChartData = computed(() => measuredDataStore.getSdi12DataList(selectedSdi12Option.value));
const sdi12ChartDataSet = computed<ChartData<'line'>>(() => ({
  datasets: sdi12ChartData.value,
}));

/**
 * SDI-12の表示項目セレクターが変化した際の処理
 * @param value
 */
const onChangeEnvironmentalSelect = (value: string) => {
  selectedEnvironmentalOption.value = value;
};

// 環境データグラフ用データ
const environmentalChartData = computed(() => measuredDataStore.getEnvironmentDataList(selectedEnvironmentalOption.value));
const environmentalChartDataSet = computed<ChartData<'line'>>(() => ({
  datasets: environmentalChartData.value,
}));
</script>

<template>
  <HeaderComponent hamburgerState :menuState="menuStateRef" @clickEvent="changeState" />
  <NavigatorComponent :menuState="menuStateRef" />
  <div class="main-content">
    <div class="graph-wrapper">
      <LineChart ref="lineRef" :chartData="sdi12ChartDataSet" :options="sdi12ChartConfig" />
      <InformationSelect
        title="表示項目"
        :option-list="sdi12OptionList"
        @selectedValue="onChangeSdi12Select"
      />
    </div>
    <div class="graph-wrapper">
      <LineChart
        ref="lineRef"
        :chartData="environmentalChartDataSet"
        :options="environmentalChartConfig"
      />
      <InformationSelect
        title="表示項目"
        :option-list="environmentalOptionList"
        @selectedValue="onChangeEnvironmentalSelect"
      />
    </div>
  </div>
</template>

<style scoped>
.main-content {
  padding: 10px;
}
</style>
