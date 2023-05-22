import { computed, ref } from 'vue';
import { MicroControllerStore } from '@/store/microControllerStore';
import { MeasuredDataStore } from '@/store/measuredDataStore';
import { useRoute } from 'vue-router';
import { Chart, ChartData, ChartOptions, registerables } from 'chart.js';
import {
  convertSdi12KeyWordToTitle,
  convertSdi12KeyWordToScale,
  convertEnvironmentalKeyWordToTitle,
  convertEnvironmentalKeyWordToScale,
} from '@/methods/measuredData';
import { NotificationType } from '@/constants/notificationType';
import { StatusCode } from '@/constants/statusCode';

export const useMeasuredData = () => {
  const fetchMeasuredData = async () => {
    await measuredDataStore.fetchMeasuredData(microControllerId).catch((e) => {
      const statusCode = e.response.status.toString();
      if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
        notificationMessage.value = 'エラーが発生しました。時間をおいて再度お試しください。';
      } else {
        notificationMessage.value =
          '予期せぬエラーが発生しました。時間をおいて再度お試しください。';
      }
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
    });
  };

  // Route
  const route = useRoute();
  const microControllerId = route.params.microControllerId[0];

  // Store
  const measuredDataStore = MeasuredDataStore();
  fetchMeasuredData();
  const microControllerStore = MicroControllerStore();
  microControllerStore.fetchMicroControllerList();

  // 初期値
  const showNotification = ref(false);
  const notificationMessage = ref('');
  const notificationType = ref(NotificationType.INFO);

  const {
    sdi12ChartConfig,
    environmentalChartConfig,
    sdi12OptionList,
    environmentalOptionList,
    onChangeSdi12Select,
    sdi12ChartDataSet,
    onChangeEnvironmentalSelect,
    environmentalChartDataSet,
  } = useChart();

  return {
    showNotification,
    notificationMessage,
    notificationType,
    sdi12ChartConfig,
    environmentalChartConfig,
    sdi12OptionList,
    environmentalOptionList,
    onChangeSdi12Select,
    sdi12ChartDataSet,
    onChangeEnvironmentalSelect,
    environmentalChartDataSet,
  };
};

export const useChart = () => {
  // 初期値
  const selectedSdi12Option = ref('');
  const selectedEnvironmentalOption = ref('');

  // Store
  const measuredDataStore = MeasuredDataStore();

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
  const sdi12ChartData = computed(() =>
    measuredDataStore.getSdi12DataList(selectedSdi12Option.value)
  );
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
  const environmentalChartData = computed(() =>
    measuredDataStore.getEnvironmentDataList(selectedEnvironmentalOption.value)
  );
  const environmentalChartDataSet = computed<ChartData<'line'>>(() => ({
    datasets: environmentalChartData.value,
  }));

  return {
    sdi12ChartConfig,
    environmentalChartConfig,
    sdi12OptionList,
    environmentalOptionList,
    onChangeSdi12Select,
    sdi12ChartDataSet,
    onChangeEnvironmentalSelect,
    environmentalChartDataSet,
  };
};
