import { shallowMount } from '@vue/test-utils';
import FormWindow from '@/components/common/FormWindow.vue';

describe('App.vue', () => {
  it('renders props.title when passed', () => {
    const title = 'new message';
    const wrapper = shallowMount(FormWindow, {
      props: { title: title },
    });
    expect(wrapper.text()).toMatch(title);
  });
});
