import { Meta, StoryObj } from '@storybook/vue3';
import CommonInput from './CommonInput.vue';

const meta: Meta<typeof CommonInput> = {
  title: 'Common/CommonInput',
  component: CommonInput,
  render: (args) => ({
    components: {
      CommonInput,
    },
    setup() {
      return { args };
    },
    template: `<CommonInput v-bind="args" />`,
  }),
  argTypes: {
    placeholder: { control: 'text' },
    errorMessage: { control: 'text' },
    type: { options: ['text', 'email', 'password'] },
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    placeholder: 'Defaultプレースホルダー',
    errorMessage: 'Defaultエラーメッセージ',
  },
};

export const Email: Story = {
  args: {
    placeholder: 'Emailプレースホルダー',
    errorMessage: 'Emailエラーメッセージ',
    type: 'email',
  },
};

export const Password: Story = {
  args: {
    placeholder: 'Passwordプレースホルダー',
    errorMessage: 'Passwordエラーメッセージ',
    type: 'password',
  },
};
