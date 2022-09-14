<template>
  <div class="minder" style="position: fixed">

    <div id="minder-container">
      <div style="position: fixed">
          <v-select class="selector"
                    :items="templates"
                    item-text="title"
                    item-value="name"
                    label="模板选择"
                    v-model="tpl"
                    dense
          ></v-select>
          <v-select class="selector"
                    :items="themes"
                    v-model="the"
                    item-text="title"
                    item-value="name"
                    label="主题选择"
                    dense
          ></v-select>
      </div>

    </div>
  </div>
</template>

<script>
import 'hotbox-ui/hotbox.css'
import '../assets/kityminder.editor.css'
import 'kityminder-core/dist/kityminder.core.css'
import 'kity'
import 'hotbox-ui'
import 'kityminder-core'
import '../assets/kityminder.editor.js'

export default {
  name: 'kityMinder',
  data() {
    return {
      minder: {},
      clickNode: {},
      tpl: 'right',
      the: "snow-compact",
      templates: [
        {
          name: 'default',
          title: '思维导图'
        },
        {
          name: 'filetree',
          title: '目录组织图'
        },
        {
          name: 'fish-bone',
          title: '鱼骨头图'
        },
        {
          name: 'right',
          title: '逻辑结构图'
        },
        {
          name: 'structure',
          title: '组织结构图'
        },
        {
          name: 'tianpan',
          title: '天盘图'
        }
      ],
      themes: [
        {
          name: 'classic',
          title: '脑图经典'
        },
        {
          name: 'classic-compact',
          title: '紧凑经典'
        },
        {
          name: 'fresh-blue',
          title: '天空蓝'
        },
        {
          name: 'fresh-blue-compat',
          title: '紧凑蓝'
        },
        {
          name: 'fresh-green',
          title: '文艺绿'
        },
        {
          name: 'fresh-green-compat',
          title: '紧凑绿'
        },
        {
          name: 'fresh-pink',
          title: '脑残粉'
        },
        {
          name: 'fresh-pink-compat',
          title: '紧凑粉'
        },
        {
          name: 'fresh-purple',
          title: '浪漫紫'
        },
        {
          name: 'fresh-purple-compat',
          title: '紧凑紫'
        },
        {
          name: 'fresh-red',
          title: '清新红'
        },
        {
          name: 'fresh-red-compat',
          title: '紧凑红'
        },
        {
          name: 'fresh-soil',
          title: '泥土黄'
        },
        {
          name: 'fresh-soil-compat',
          title: '紧凑黄'
        },
        {
          name: 'snow',
          title: '温柔冷光'
        },
        {
          name: 'snow-compact',
          title: '紧凑冷光'
        },
        {
          name: 'tianpan',
          title: '经典天盘'
        },
        {
          name: 'tianpan-compact',
          title: '紧凑天盘'
        },
        {
          name: 'fish',
          title: '鱼骨图'
        },
        {
          name: 'wire',
          title: '线框'
        }
      ],
    }
  },
  props: {
    importJson: {
      type: Object,
      default: () => []
    }
  },
  mounted() {
    this.KMEditor = new window.kityminder.Editor(document.querySelector('#minder-container'))
    this.minder = this.KMEditor.minder
    this.minder.importJson(this.importJson)
    this.minder.execCommand('Template', 'right')
    this.$emit('minder', this.minder)


    this.minder.on('selectionchange', () => {
      this.selectedNodes = this.minder.getSelectedNodes().length;
      // if (this.selectedNodes === 1) {
      //   this.text = this.minder.getSelectedNode().getText();
      // } else {
      //   this.text = '';
      // }
      //
      // this.$emit('selection-change', this.getNodeData());
    });
    this.minder.on('contentchange', (e) => {
      console.log('You contentchange: "%s"', e);
      console.log(e);
      console.log(' contentchange You selected: "%s"', this.minder.getSelectedNodes());

      // this.$emit('selection-change', this.getNodeData());
    });
    this.minder.on('click', () => {
      if (this.selectedNodes === 1) {
        this.clickNode = this.minder.getSelectedNode().getData();
        console.log(this.clickNode);
        this.$emit('selection-change', this.clickNode);

        console.log(this.clickNode.children);
      } else {
        this.text = '';
      }
      //
      // this.$emit('selection-change', this.getNodeData());
    });

  },
  watch: {
    template(val) {
      this.tpl = val;
    },
    tpl(val) {
      this.handleTemplate(val);
    },
    theme(val) {
      this.the = val;
    },
    the(val) {
      this.handleTheme(val);
    }
  },
  methods: {
    handleTemplate(template) {
      console.log(template)
      this.minder.execCommand('Template', template);
      this.$emit('template-change', template);
    },
    handleTheme(theme) {
      console.log(theme)
      // snow-compact
      this.minder.execCommand('Theme', theme);
      this.$emit('theme-change', theme);
    },
    updateData(data) {
      this.minder.importJson(data)
    }
  }
}
</script>

<style scoped>
#minder-container {
  height: 100vh;
  width: 100vw;
}

.selector {
  margin: 20px;
}
</style>
