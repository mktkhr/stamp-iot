import { Meta, StoryObj } from '@storybook/vue3';
import CommonDialog from './CommonDialog.vue';

const meta: Meta<typeof CommonDialog> = {
  title: 'Common/CommonDialog',
  component: CommonDialog,
  render: (args) => ({
    components: { CommonDialog },
    setup() {
      return { args };
    },
    // TODO: buttonスロットにコンポーネントを入れたい
    template: `
      <CommonDialog v-bind="args" />
    `,
  }),
  // Props
  argTypes: {
    title: { control: 'text' },
    description: { control: 'text' },
    useButton: { control: 'boolean' },
  },
  args: {},
  parameters: {
    backgrounds: {
      values: [
        { name: 'Dark', value: '#333' },
        { name: 'Light', value: 'white' },
      ],
      default: 'Dark', // 背景が白だとコンポーネントが見えないので，背景を黒に設定
    },
  },
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    title: 'ダイアログタイトル',
    description: 'ダイアログの説明書き',
  },
};
