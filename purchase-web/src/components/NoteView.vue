<template>
    <!-- <div v-html="noteHtml">
    </div> -->
     <mavon-editor
        v-model="noteC"
        style="width:100%;z-index: 10;"
        :ishljs="true"
        :externalLink="externalLink"
        :editable="false"
        :toolbarsFlag="false"
        :shortCut="false"
        defaultOpen="preview"
        :subfield="false"
    />
</template>

<script lang="ts">
import { defineComponent,ref,reactive } from "vue";
// import { marked } from 'marked'


var mavonEditor = require('mavon-editor')
import 'mavon-editor/dist/css/index.css'

export default defineComponent({
    components: {
        "mavon-editor": mavonEditor.mavonEditor,
    },
    props: ['viewNote'],
    setup(props) {
        let noteC = ref(props.viewNote)
        // let noteHtml = ref(marked(noteC.value))
        const externalLink = reactive({
            markdown_css: function () {
                return 'https://cdn.bootcdn.net/ajax/libs/github-markdown-css/5.1.0/github-markdown.min.css';
            },
            hljs_js: function () {
                // return 'https://cdn.bootcdn.net/ajax/libs/highlight.js/11.5.0/highlight.min.js';
                return 'https://cdn.bootcdn.net/ajax/libs/highlight.js/10.6.0/highlight.min.js';
            },
            hljs_lang: function (lang: any) {
                return (
                    'https://cdn.bootcdn.net/ajax/libs/highlight.js/10.6.0/languages/' +
                    lang +
                    '.min.js'
                );
            },
            hljs_css: function (css: any) {
                if (css) {
                    return (
                        'https://cdn.bootcdn.net/ajax/libs/highlight.js/10.6.0/styles/' +
                        css +
                        '.min.css'
                    );
                }
                return '';
            },
            katex_js: function () {
                return 'https://cdn.bootcdn.net/ajax/libs/KaTeX/0.8.3/katex.min.js';
            },
            katex_css: function () {
                return 'https://cdn.bootcdn.net/ajax/libs/KaTeX/0.8.3/katex.min.css';
            },
        })
        return {
            noteC,
            // noteHtml,
            externalLink,
        }
    }
})
</script>