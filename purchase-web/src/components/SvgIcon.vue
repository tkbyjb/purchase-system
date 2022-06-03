<template>
    <!-- <object type="image/svg+xml" :data="url" :style="{ width: iconWidth + 'px' }"></object> -->

    <svg  aria-hidden="true"  :style="{ width: iconWidth + 'px', height: iconWidth + 'px', fill: iconColor}">
        <use :xlink:href="'#icon-'+iconName" />
    </svg>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
    props: {
        iconName: {
            type: String,
        },
        iconWidth: {
            type: Number,
        },
        iconColor: {
            type: String,
        }
    },
    // setup(props) {
    //     const url = ref('')
    //     onMounted(() => {
    //         import('@/assets/iconsvg/' + props.icon + '.svg')
    //             .then((resp: any) => {
    //                 url.value = resp.default
    //             })
    //     })
    //     return {
    //         url
    //     }
    // }
})
</script>
