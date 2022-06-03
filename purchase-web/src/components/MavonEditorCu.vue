<template>
<!--  <mavon-editor-->
<!--      v-model="md"-->
<!--      :externalLink="externalLink"-->
<!--      :ishljs="true"-->
<!--      style="height:500px;width:100%;z-index: 10;"-->
<!--      @imgAdd="$imgAdd"-->
<!--      @imgDel="$imgDel"-->
<!--  />-->

  <md-editor v-model="md" style="height:500px;width:100%;" @onUploadImg="onUploadImg"/>
</template>

<script lang="ts">
import {defineComponent, reactive, ref} from "vue";
// import 'mavon-editor/dist/css/index.css'
// var mavonEditor = require('mavon-editor')

import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

export default defineComponent({
  components: {
    // "mavon-editor": mavonEditor.mavonEditor,
    MdEditor
  },
  setup() {
    let md = ref('')
    // const externalLink = reactive({
    //   markdown_css: function () {
    //     return 'https://cdn.bootcdn.net/ajax/libs/github-markdown-css/5.1.0/github-markdown.min.css';
    //   },
    //   hljs_js: function () {
    //     // return 'https://cdn.bootcdn.net/ajax/libs/highlight.js/11.5.0/highlight.min.js';
    //     return 'https://cdn.bootcdn.net/ajax/libs/highlight.js/10.6.0/highlight.min.js';
    //   },
    //   hljs_lang: function (lang: any) {
    //     return (
    //         'https://cdn.bootcdn.net/ajax/libs/highlight.js/10.6.0/languages/' +
    //         lang +
    //         '.min.js'
    //     );
    //   },
    //   hljs_css: function (css: any) {
    //     if (css) {
    //       return (
    //           'https://cdn.bootcdn.net/ajax/libs/highlight.js/10.6.0/styles/' +
    //           css +
    //           '.min.css'
    //       );
    //     }
    //     return '';
    //   },
    //   katex_js: function () {
    //     return 'https://cdn.bootcdn.net/ajax/libs/KaTeX/0.8.3/katex.min.js';
    //   },
    //   katex_css: function () {
    //     return 'https://cdn.bootcdn.net/ajax/libs/KaTeX/0.8.3/katex.min.css';
    //   },
    // })

    function getMd(): string {
      return md.value;
    }

    function clear(): void {
      md.value = ""
    }

    function setMd(note: string): void {
      md.value = note
    }

    // function $imgAdd(pos: any, $file: any) {
    //   console.log('^^^^^^^^^', pos, $file)
    // }
    // function $imgDel(pos: any) {
    //   console.log('^^^^^^^^^', pos)
    // }
    const onUploadImg = async (files: any, callback: any) => {
      console.log(files)
      // const res = await Promise.all(
      //     files.map((file: any) => {
      //       return new Promise((rev, rej) => {
      //         const form = new FormData();
      //         form.append('file', file);
      //       });
      //     })
      // );

      // callback(res.map((item) => item.data.url));
    };
    return {
      // externalLink,
      md,
      getMd,
      clear,
      setMd,
      // $imgAdd,
      // $imgDel,
      onUploadImg,
    }
  }
})
</script>
