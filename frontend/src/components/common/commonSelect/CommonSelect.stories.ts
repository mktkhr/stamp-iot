import { Meta, StoryObj } from '@storybook/vue3';
import CommonSelect from './CommonSelect.vue';
import { SelectOptionType } from './composable';

const meta: Meta<typeof CommonSelect> = {
  title: 'Common/CommonSelect',
  component: CommonSelect,
  render: (args) => ({
    components: {
      CommonSelect,
    },
    setup() {
      return { args };
    },
    template: `<CommonSelect v-bind="args" />`,
  }),
  argTypes: {
    placeholder: { control: 'text' },
    errorMessage: { control: 'text' },
    useDummyOption: { control: 'boolean' },
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

const customOptionList: SelectOptionType[] = [
  { key: 'value1', word: '選択肢1' },
  { key: 'value2', word: '選択肢2' },
  { key: 'value3', word: '選択肢3' },
];

export const Default: Story = {
  args: {
    placeholder: 'Defaultプレースホルダー',
    errorMessage: 'Defaultエラーメッセージ',
    optionList: customOptionList,
  },
};
