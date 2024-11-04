import { Meta, StoryObj } from '@storybook/vue3';
import { onMounted, ref } from 'vue';
import CommonButton from '../commonButton/CommonButton.vue';
import CommonDialog from '../commonDialog/CommonDialog.vue';
import CommonOverlay from './CommonOverlay.vue';

const meta: Meta<typeof CommonOverlay> = {
  title: 'Common/CommonOverlay',
  component: CommonOverlay,
  render: (args) => ({
    components: {
      CommonOverlay,
      CommonButton,
      CommonDialog,
    },
    setup() {
      // レンダリングを待つ必要があるので，Storybook用にフラグを持たせる
      const isMounted = ref(false);
      onMounted(() => {
        // 0.1秒待ってダイアログを表示させる
        setTimeout(() => {
          isMounted.value = true;
        }, 100);
      });
      return { args, isMounted };
    },
    template: `
      <CommonOverlay v-model="isMounted" v-bind="args">
        <CommonDialog
          title="コンテンツタイトル"
          description="コンテンツ内容"
        >
          <CommonButton button-title="コンテンツスロット" />
        </CommonDialog>
      </CommonOverlay>
    `,
  }),
  decorators: [],
  args: {
    teleportDestination: '#app',
    customBackgroundClickEvent: null,
    persistent: false,
    hidePersistentAnimation: false,
  },
  argTypes: {
    teleportDestination: {
      control: 'text',
      description: 'Overlayのテレポート先',
    },
    backgroudColor: {
      control: 'color',
      description: 'Overlayコンテンツの外側の背景色',
    },
    persistent: {
      control: 'boolean',
      description: 'Overlayコンテンツの外側クリック時にOverlayを閉じるか否か',
    },
    hidePersistentAnimation: {
      control: 'boolean',
      description: 'Persistent時のOverlayを閉じられないアニメーションの非表示設定',
    },
    maxWidth: { control: 'text', description: 'Overlayコンテンツの最大幅' },
    maxHeight: { control: 'text', description: 'Overlayコンテンツの最大高さ' },
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: { teleportDestination: '#app', persistent: false },
};

export const Persistent: Story = {
  args: { teleportDestination: '#app', persistent: true },
};
