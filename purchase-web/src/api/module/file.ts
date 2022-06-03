import request from '@/api/request'
import {AxiosPromise} from 'axios'

// function uploadApplyAddFile(formData: FormData): AxiosPromise{
//     return request({
//         url: '/apply/department/add',
//         method: 'post',
//         // headers: {
//         //     'Content-type' : 'multipart/form-data'
//         // },
//         data: formData,
//     })
// }

function getContractUrl(purchaseId: string): AxiosPromise{
    //获取一个采购的合同下载路径
    return request({
        url: '/file/get/contractUrl',
        method: 'get',
        params:{
            purchaseId,
        }
    })
}
export default {
    // uploadApplyAddFile,
    getContractUrl,
}
