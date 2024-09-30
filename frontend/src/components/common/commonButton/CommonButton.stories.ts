import { Meta, StoryObj } from '@storybook/vue3';
import CommonButton from './CommonButton.vue';

const meta: Meta<typeof CommonButton> = {
  title: 'Common/CommonButton',
  component: CommonButton,
  render: (args) => ({
    components: { CommonButton },
    setup() {
      return { args };
    },
    template: `<CommonButton v-bind="args" />`,
  }),
  // Props
  argTypes: {
    buttonTitle: { control: 'text' },
    height: { control: 'text' },
    width: { control: 'text' },
    type: {
      options: ['fill', 'text', 'float', 'outlined'],
    },
    color: { control: 'color' },
    titleColor: { control: 'color' },
    icon: { control: 'text' },
    fontSize: { control: 'text' },
    fontWeight: {
      options: ['normal', 'bold'],
    },
    justifyContent: {
      options: [
        'center',
        'flex-start',
        'flex-end',
        'space-between',
        'space-around',
        'space-evenly',
      ],
    },
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    buttonTitle: 'デフォルト',
  },
};

export const Text: Story = {
  args: {
    buttonTitle: 'テキスト',
    type: 'text',
  },
};

export const Fill: Story = {
  args: {
    buttonTitle: 'フィル',
    type: 'fill',
  },
};

export const Outlined: Story = {
  args: {
    buttonTitle: 'アウトライン',
    type: 'outlined',
  },
};
