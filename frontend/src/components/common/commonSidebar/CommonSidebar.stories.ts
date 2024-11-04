import { Meta, StoryObj } from '@storybook/vue3';
import { onMounted, ref } from 'vue';
import CommonSidebar from './CommonSidebar.vue';
import SidebarTile from './components/sidebarTile/SidebarTile.vue';

const meta: Meta<typeof CommonSidebar> = {
  title: 'Common/CommonSidebar',
  component: CommonSidebar,
  render: (args) => ({
    components: {
      CommonSidebar,
      SidebarTile,
    },
    setup() {
      // レンダリングを待つ必要があるので，Storybook用にフラグを持たせる
      const isMounted = ref(false);
      onMounted(() => {
        isMounted.value = true;
      });

      // TODO: previewの再読み込み時にエラーが発生するので，修正する(エラー: Cannot GET /login)
      return { args, isMounted };
    },
    template: `<CommonSidebar v-if="isMounted" v-bind="args" />`,
  }),
  parameters: {
    backgrounds: {
      values: [
        { name: 'Dark', value: '#333' },
        { name: 'Light', value: 'white' },
      ],
      default: 'Dark', // 背景が白だとコンポーネントが見えないので，背景を黒に設定
    },
  },
  tags: ['autodocs'],
  decorators: [],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
