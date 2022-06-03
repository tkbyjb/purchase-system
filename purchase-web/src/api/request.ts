import ResultState from '@/type/ResultState';
import axios from 'axios'
import { AxiosError } from 'axios'
import { ElMessage } from 'element-plus'
import { createApp } from 'vue'
import Progress from '@/components/Progress.vue'

const toast = createApp(Progress)

axios.defaults.baseURL = '/api'
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.timeout = 20000;


axios.interceptors.request.use(function (config: any) {
    if(config.url.indexOf("public") == -1 ) {
        const AUTH_TOKEN: string = localStorage.getItem('Authorization') as string;
        config.headers.Authorization = AUTH_TOKEN;
    }
    toast.mount('#headerProgress')
    return config;
}, function (error) {
    return Promise.reject(error);
})
axios.defaults.validateStatus = (status: number) => {
    return /^(2|3)\d{2}$/.test(status.toString());
}
axios.interceptors.response.use(function (response) {
    toast.unmount()
    if (response.data.mess != null && response.data.mess != '') {
        if(response.data.state == ResultState.SUCCESS){
            ElMessage({
                message: response.data.mess,
                type: 'success',
            })
        }else{
            ElMessage({
                message: response.data.mess,
                type: 'warning',
            })
        }
    }
    return response;
}, function (error: AxiosError) {
    //默认响应码处理
    const { response } = error;
    if (response) {
        if (/^4/.test(response.status.toString())) {
            ElMessage({
                message: '客户端错误: 您没有权限或请求不合法',
                type: 'error',
            })
        } else if (/^5/.test(response.status.toString())) {
            ElMessage({
                message: '服务器错误',
                type: 'error',
            })
        }
    } else {
        if (!window.navigator.onLine) {
            ElMessage({
                message: '网络错误: 请检查您的网络',
                type: 'error',
            })
        }
        ElMessage({
            message: error.message,
            type: 'error',
        })
        return Promise.reject(error);
    }
})


export default axios;