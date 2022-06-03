import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import api from '@/api/api'
import ResultState from '@/type/ResultState'
import md5 from 'js-md5';


import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import "/node_modules/primeflex/primeflex.css"
import PrimeVue from 'primevue/config';
import "primevue/resources/themes/lara-light-blue/theme.css";
import "primevue/resources/primevue.min.css";
import 'primeicons/primeicons.css';
import PrimeDialog from 'primevue/dialog';
import LeftLabel from '@/components/LeftLabel.vue'

import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

import SvgIcon from '@/components/SvgIcon.vue'
// import mavonEditor from 'mavon-editor'


const app = createApp(App)
app.config.globalProperties.$api = api
app.config.globalProperties.$state = ResultState
app.config.globalProperties.$md5 = md5


// 屏蔽错误信息
// app.config.errorHandler = () => null;
// 屏蔽警告信息
app.config.warnHandler = () => null;


app.use(store)
    .use(PrimeVue, {ripple: true})
    .use(router)
    .use(ElementPlus)
    .use(Antd)
    // .use(mavonEditor)
    .component('SvgIcon', SvgIcon)
    .component('PrimeDialog', PrimeDialog)
    .component('LeftLabel', LeftLabel)
    .mount('#app')

const svgIcons = require.context('@/static/iconsvg/', false, /\.svg$/)
const requireAll = (requireContext: any) => requireContext.keys().map(requireContext)
requireAll(svgIcons)
