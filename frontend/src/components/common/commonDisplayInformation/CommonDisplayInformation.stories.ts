import { Meta, StoryObj } from '@storybook/vue3';
import CommonButton from '../commonButton/CommonButton.vue';
import CommonDisplayInformation from './CommonDisplayInformation.vue';

const meta: Meta<typeof CommonDisplayInformation> = {
  title: 'Common/CommonDisplayInformation',
  component: CommonDisplayInformation,
  render: (args) => ({
    components: { CommonDisplayInformation, CommonButton },
    setup() {
      return { args };
    },
    template: `
      <CommonDisplayInformation v-bind="args" />
    `,
  }),
  // Props
  argTypes: {
    title: { control: 'text' },
    content: { control: 'text' },
    height: { control: 'text' },
    titleWidth: { control: 'text' },
    titleBackGroundColor: { control: 'color' },
    contentBackGroundColor: { control: 'color' },
    showBorderTop: { control: 'boolean' },
    showBorderBottom: { control: 'boolean' },
    isEditMode: { control: 'boolean' },
  },
  args: {
    title: 'タイトル',
    content: 'コンテンツ',
  },
  parameters: {
    // TODO: スロットにコンポーネントを描画したい
    slots: {
      title: {
        description: 'Totle slot',
        template: `<p>タイトルスロット</p>`,
      },
      content: {
        description: 'Content slot',
        components: { CommonButton },
        template: `<CommonButton :button-title="コンテンツスロット" />`,
      },
    },
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    title: 'タイトル',
    content: 'コンテンツ',
  },
};
