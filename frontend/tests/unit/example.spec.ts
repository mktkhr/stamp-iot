import SidebarTile from '@/components/common/commonSidebar/components/sidebarTile/SidebarTile.vue';
import { shallowMount } from '@vue/test-utils';

describe('App.vue', () => {
  it('renders props.title when passed', () => {
    const title = 'new message';
    const wrapper = shallowMount(SidebarTile, {
      props: { title: title },
    });
    expect(wrapper.text()).toMatch(title);
  });
});
