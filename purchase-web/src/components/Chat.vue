<template>
  <div class="purchase-chat-dialog-content grid">
    <div class="col-3 purchase-chat-dialog-content-left">
      <!--      <div>-->
      <!--        <el-input v-model="input4" placeholder="搜索" size="small" style="width:95%;margin-left:4px;">-->
      <!--          <template #suffix>-->
      <!--            <SvgIcon :iconWidth="15" iconColor="gray" iconName="search"/>-->
      <!--          </template>-->
      <!--        </el-input>-->
      <!--      </div>-->
      <div style="margin-top:10px;">
        <div v-for="(item) in leftinfos"
             :key="item.userId"
             style="border-bottom: 1px solid gray; padding:3px;cursor:pointer;"
             @click="getMess(item)"
             @contextmenu.prevent=""
        >
          <div class="flex align-items-center justify-content-center">
            <el-avatar
                :class="{unOnline: item.isOnline==0}"
                :size="32"
                :src="item.avatar"
                style="border:1px solid #3b82f6;"
            />
            <el-badge :value="item.unreadMessCount==0?undefined:item.unreadMessCount">
              <span style="font-size: 80%;margin-left:3px;">{{ item.realname }}</span>
            </el-badge>
          </div>
          <span style="font-size: 50%;margin-left:8px;">{{ item.newestMessTime }}</span>
        </div>
      </div>
    </div>
    <div class="col-9 purchase-chat-dialog-content-right">
      <div style="padding-bottom: 4px;background-color:#ebebeb;">
        <span style="font-size: 90%;margin-left:10px;">{{ messs.realname }}</span>
      </div>
      <div class="chat-content">
        <div v-for="(item) in messs.messs"
             :key="item" :class="{
          'chat-isme-item': item.type==1,
          'chat-notme-item': item.type==0,
          'justify-content-end': item.type==1,
          'flex-row-reverse': item.type==1
        }"
             class="chat-view-item flex align-items-center">
          <el-avatar
              :class="{unOnline: messs.isOnline==0&&item.type==0}"
              :size="32"
              :src="item.type==0?messs.avatar:selfavatar"
              style="border:1px solid #3b82f6;"
          />
          <span class="chat-view-item-mess">{{ item.mess }}</span>
          <span style="font-size:60%;">{{ item.time }}</span>
        </div>
      </div>
      <div class="chat-input">
        <div class="flex justify-content-end" style="margin-bottom: 3px;margin-top:3px;">
          <!--                    <el-button size="small" type="warning">发送文件</el-button>-->
          <el-button size="small" style="margin-right: 10px;" type="primary" @click="sendMess">发送消息</el-button>
        </div>
        <a-textarea
            v-model:value="inputmess"
            :maxlength="360"
            :rows="3"
            placeholder="请输入..."
            resize="none"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref} from 'vue'
import {useRoute} from 'vue-router'
import {useStore} from 'vuex'

export default defineComponent({
  setup() {
    const {proxy}: any = getCurrentInstance()
    const route = useRoute()
    const store = useStore()

    let leftinfos = ref<Array<any>>([])

    function getLeft(): void {
      //获取聊天列表左边信息
      proxy.$api.chat.getChatLeft()
          .then((response: any) => {
            console.log('@@@@@@', response.data)
            leftinfos.value = response.data.data
          })
    }

    onMounted(() => {
      getLeft()
    })
    let messs = reactive({
      userId: '',
      realname: '',
      isOnline: 0,
      avatar: '',
      messs: []
    })

    function getMess(item: any): void {
      //点击左侧获取右边的信息,并把数据库未读改为已读
      messs.userId = item.userId
      messs.realname = item.realname
      messs.isOnline = item.isOnline
      messs.avatar = item.avatar
      proxy.$api.chat.getMesss(item.userId)
          .then((response: any) => {
            messs.messs = response.data.data
            getLeft()
          })
    }

    let selfavatar = ref(localStorage.getItem("avatar"))
    let selfId = localStorage.getItem("id")

    const socket: WebSocket = new WebSocket("ws://localhost:9990/chat/" + selfId);//这里就会去连接
    socket.onopen = () => {
      console.log("连接成功了")
    }
    socket.onmessage = function (msg: any) {
      //接收消息
      console.log("接收到了消息  ", msg)
    };
    socket.onclose = function () {
      console.log("websocket已关闭");
    };
    socket.onerror = function () {
      console.log("websocket发生了错误");
    }

    let inputmess = ref('')
    function sendMess(): void {
      //点击发送消息
      let param = {
        receiveUserId: messs.userId,//发给谁
        mess: inputmess.value,
        sendUserId: selfId,
      }
      socket.send(JSON.stringify(param))
      // proxy.$api.chat.getMesss(messs.userId)
      //     .then((response: any) => {
      //       messs.messs = response.data.data
      //       getLeft()
      //     })
    }

    return {
      route,
      store,
      proxy,
      getLeft,
      leftinfos,
      getMess,
      messs,
      selfavatar,
      selfId,
      sendMess,
      inputmess,
      socket,
    }
  }
})
</script>

<style lang="scss" scoped>
.unOnline {
  filter: grayscale(100%);
}

.chat-notme-item {
  .chat-view-item-mess {
    font-size: 70%;
    background-color: #e9f1fe;
    margin-left: 5px;
    border-radius: 10px;
    padding: 6px;
    max-width: 250px;
  }
}

.chat-isme-item {
  .chat-view-item-mess {
    font-size: 70%;
    background-color: #9eeb6bff;
    margin-right: 5px;
    border-radius: 10px;
    padding: 6px;
    max-width: 250px;
  }
}

.chat-view-item {
  margin: 5px;
  wdith: 100%;
}

.chat-content {
  height: 65%;
}

.chat-input {
  // border-top: 1px solid rgb(218, 218, 218);
  // background-color: #e7e7e7;
}

.chat-user {
  border-bottom: 1px dashed rgb(218, 218, 218);
  padding-top: 5px;
  padding-bottom: 5px;
  cursor: pointer;
  // background-color: rgb(240, 243, 255);
}

.purchase-chat-dialog-content-right {
  background-color: white;
}

.purchase-chat-dialog-content-left {
  background-color: #f5f5f5ff;
}

.grid {
  margin: 0 !important;
}

.routerlinks {
  text-decoration: none;
  color: #3b82f6;
}

.purchase-chat-dialog-content {
  width: 100%;
  height: 100%;
  background-color: #f5f5f5ff;
}
</style>
<style lang="scss">
.purchase-chat-dialog-content-right::-webkit-scrollbar {
  width: 4px;
  height: 10px;
  background: white; /*设置轨道颜色*/
  padding-right: 2px;
}

.purchase-chat-dialog-content-right::-webkit-scrollbar-thumb {
  background: #e2e3e5;
  border-radius: 10px;
}

.purchase-chat-dialog-content-left::-webkit-scrollbar {
  width: 4px;
  height: 10px;
  background: white; /*设置轨道颜色*/
  padding-right: 2px;
}

.purchase-chat-dialog-content-left::-webkit-scrollbar-thumb {
  background: #e2e3e5;
  border-radius: 10px;
}

.purchase-chat-dialog-content-right.col-9 {
  margin: 0;
  padding: 0;
}

// .chat-input{
/deep/ .el-textarea {
  background-color: red;
  border: 3px solid red;
}

// }
.chat-content::-webkit-scrollbar {
  width: 4px;
  height: 10px;
  background: white; /*设置轨道颜色*/
  padding-right: 2px;
}

.chat-content::-webkit-scrollbar-thumb {
  background: #e2e3e5;
  border-radius: 10px;
}

.purchase-chat-dialog-content-left.col-3 {
  padding: 0;
}
</style>
