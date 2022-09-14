<template>
  <div>    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">分类</h1>
    </div>
    <kityMinder id="minder" ref="mind" :importJson="importJson" @minder="minderHandle" @selection-change="clickNode"/>

    <div>
      <v-navigation-drawer style="position: absolute;padding: 10px 10px"
                           width="30%"
                           v-model="drawer"
                           absolute
                           temporary
                           right
      >
        <Article :article-list="articleList"></Article>
        <!-- 无限加载 -->
        <infinite-loading @infinite="infiniteHandler" ref="infiniteLoading" spinner="waveDots">
          <div slot="no-more"/>
        </infinite-loading>

      </v-navigation-drawer>
    </div>

  </div>


</template>

<script>
import kityMinder from "@/components/kityMinder";
import Article from "@/components/Article";

export default {
  created() {
    this.getCategoryList();
  },
  components: {
    Article,
    kityMinder
  },
  data() {
    return {
      current: 1,
      currentCategoryId: null,
      drawer: null,
      articleList: [],
      items: [
        {title: 'Home', icon: 'mdi-view-dashboard'},
        {title: 'About', icon: 'mdi-forum'},
      ],
      importJson: {
        'data': {'text': '分类', 'id': -1, 'currentstyle': 'snow-compact'},
        'children': []
      }
    };
  },
  methods: {
    minderHandle(data) { //
      console.log(data);
    },
    getCategoryList() {
      this.axios.get("/api/admin/categories/tree")
          .then(({data}) => {
            if (data.flag) {
              this.importJson = {
                'data': {'text': '分类', 'id': -1, 'currentstyle': 'snow-compact'},
                'children': data.data
              }
              this.$refs.mind.updateData(this.importJson)
            }
          });
    },
    clickNode(node) {
      this.articleList = []
      this.current = 1
      this.currentCategoryId = node.id
      this.drawer = !this.drawer
      this.infiniteHandler(this.$refs.infiniteLoading.stateChanger)
    },
    infiniteHandler($state) {
      console.log("infiniteHandler...")
      console.log($state)
      let md = require("markdown-it")();
      if (this.currentCategoryId === null) {
        return
      }
      this.axios
          .get("/api/articles/con", {
            params: {
              categoryId: this.currentCategoryId,
              current: this.current
            }
          })
          .then(({data}) => {
            if (data.data.length) {
              // 去除markdown标签
              data.data.forEach(item => {
                item.articleContent = md
                    .render(item.articleContent)
                    .replace(/<\/?[^>]*>/g, "")
                    .replace(/[|]*\n/, "")
                    .replace(/&npsp;/gi, "");
              });
              this.articleList.push(...data.data);
              this.current++;
              if ($state) {
                $state.loaded();
              }
            } else {
              if ($state) {
                $state.complete();
              }
            }
          });
    }
  },
  computed: {
    cover() {
      var cover = "";
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel == "category") {
          cover = item.pageCover;
        }
      });
      return "background: url(" + cover + ") center center / cover no-repeat";
    }
  }
}
</script>

<style scoped>

</style>
