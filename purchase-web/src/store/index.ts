import { createStore } from 'vuex'

interface Tab {
  title: string;
  name: string;
  content: string;
}
export default createStore({
  state: {
    isCollapse: false,
    tabs: [],
    tabValue: '',
  },
  mutations: {
    editIsCollapse(state: any, isCollapse: boolean) {
      state.isCollapse = isCollapse
    },
    addTab(state: any, newTab: any) {
      for (const tab of state.tabs) {
        if (tab.name == newTab.name) {
          state.tabValue = newTab.name
          return
        }
      }
      state.tabs.push(newTab)
      state.tabValue = newTab.name
    },
    removeTab(state: any, targetName: string) {
      state.tabs.forEach((tab: Tab, index: number) => {
        if (tab.name == targetName) {
          const nextTab = state.tabs[index + 1] || state.tabs[index - 1]
          state.tabs.splice(index, 1)
          if (nextTab) {
            state.tabValue = nextTab.name
          }
        }
      })
    },
    editTabValue(state: any, newTabValue) {
      state.tabValue = newTabValue;
    },
  },
  actions: {
  },
  modules: {
  }
})
