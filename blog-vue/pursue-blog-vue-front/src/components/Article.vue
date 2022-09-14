<template>
  <div>
    <v-card
        class="animated zoomIn article-card"
        style="border-radius: 12px 8px 8px 12px"
        v-for="(item, index) of articleList"
        :key="item.id"
    >
      <!-- 文章封面图 -->
      <div :class="isRight(index)">
        <router-link :to="'/articles/' + item.id">
          <v-img
              class="on-hover"
              width="100%"
              height="100%"
              :src="item.articleCover"
          />
        </router-link>
      </div>
      <!-- 文章信息 -->
      <div class="article-wrapper">
        <div style="line-height:1.4">
          <router-link :to="'/articles/' + item.id">
            {{ item.articleTitle }}
          </router-link>
        </div>
        <div class="article-info">
          <!-- 是否置顶 -->
          <span v-if="item.isTop == 1">
                <span style="color:#ff7242">
                  <i class="iconfont iconzhiding"/> 置顶
                </span>
                <span class="separator">|</span>
              </span>
          <!-- 发表时间 -->
          <v-icon size="14">mdi-calendar-month-outline</v-icon>
          {{ item.createTime | date }}
          <span class="separator">|</span>
          <!-- 文章分类 -->
          <router-link :to="'/categories/' + item.categoryId">
            <v-icon size="14">mdi-inbox-full</v-icon>
            {{ item.categoryName }}
          </router-link>
          <span class="separator">|</span>
          <!-- 文章标签 -->
          <router-link
              style="display:inline-block"
              :to="'/tags/' + tag.id"
              class="mr-1"
              v-for="tag of item.tagDTOList"
              :key="tag.id"
          >
            <v-icon size="14">mdi-tag-multiple</v-icon>
            {{ tag.tagName }}
          </router-link>
        </div>
        <!-- 文章内容 -->
        <div class="article-content">
          {{ item.articleContent }}
        </div>
      </div>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "Article",
  props: {
    articleList: {
      default: () => []
    }
  },
  computed: {
    isRight() {
      return function(index) {
        if (index % 2 == 0) {
          return "article-cover left-radius";
        }
        return "article-cover right-radius";
      };
    },
  }
}
</script>


<style scoped>
.home-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
  background-attachment: fixed;
  text-align: center;
  color: #fff !important;
  animation: header-effect 1s;
}
.banner-container {
  margin-top: 43vh;
  line-height: 1.5;
  color: #eee;
}
.blog-contact a {
  color: #fff !important;
}
.card-info-social {
  line-height: 40px;
  text-align: center;
  margin: 6px 0 -6px;
}
.card-info-social a {
  font-size: 1.5rem;
}
.left-radius {
  border-radius: 8px 0 0 8px !important;
  order: 0;
}
.right-radius {
  border-radius: 0 8px 8px 0 !important;
  order: 1;
}
.article-wrapper {
  font-size: 14px;
}
@media (min-width: 760px) {
  .blog-title {
    font-size: 2.5rem;
  }
  .blog-intro {
    font-size: 1.5rem;
  }
  .blog-contact {
    display: none;
  }
  .home-container {
    max-width: 1200px;
    margin: calc(100vh - 48px) auto 28px auto;
    padding: 0 5px;
  }
  .article-card {
    display: flex;
    align-items: center;
    height: 280px;
    width: 100%;
    margin-top: 20px;
  }
  .article-cover {
    overflow: hidden;
    height: 100%;
    width: 45%;
  }
  .on-hover {
    transition: all 0.6s;
  }
  .article-card:hover .on-hover {
    transform: scale(1.1);
  }
  .article-wrapper {
    padding: 0 2.5rem;
    width: 55%;
  }
  .article-wrapper a {
    font-size: 1.5rem;
    transition: all 0.3s;
  }
}
@media (max-width: 759px) {
  .blog-title {
    font-size: 26px;
  }
  .blog-contact {
    font-size: 1.25rem;
    line-height: 2;
  }
  .home-container {
    width: 100%;
    margin: calc(100vh - 66px) auto 0 auto;
  }
  .article-card {
    margin-top: 1rem;
  }
  .article-cover {
    border-radius: 8px 8px 0 0 !important;
    height: 230px !important;
    width: 100%;
  }
  .article-cover div {
    border-radius: 8px 8px 0 0 !important;
  }
  .article-wrapper {
    padding: 1.25rem 1.25rem 1.875rem;
  }
  .article-wrapper a {
    font-size: 1.25rem;
    transition: all 0.3s;
  }
}
.scroll-down {
  cursor: pointer;
  position: absolute;
  bottom: 0;
  width: 100%;
}
.scroll-down i {
  font-size: 2rem;
}
.article-wrapper a:hover {
  color: #8e8cd8;
}
.article-info {
  font-size: 95%;
  color: #858585;
  line-height: 2;
  margin: 0.375rem 0;
}
.article-info a {
  font-size: 95%;
  color: #858585 !important;
}
.article-content {
  line-height: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.blog-wrapper {
  position: sticky;
  top: 10px;
}
.blog-card {
  line-height: 2;
  padding: 1.25rem 1.5rem;
}
.author-wrapper {
  text-align: center;
}
.blog-info-wrapper {
  display: flex;
  justify-self: center;
  padding: 0.875rem 0;
}
.blog-info-data {
  flex: 1;
  text-align: center;
}
.blog-info-data a {
  text-decoration: none;
}
.collection-btn {
  text-align: center;
  z-index: 1;
  font-size: 14px;
  position: relative;
  display: block;
  background-color: #49b1f5;
  color: #fff !important;
  height: 32px;
  line-height: 32px;
  transition-duration: 1s;
  transition-property: color;
}
.collection-btn:before {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: -1;
  background: #ff7242;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.5s;
  transition-property: transform;
  transform: scaleX(0);
  transform-origin: 0 50%;
}
.collection-btn:hover:before {
  transition-timing-function: cubic-bezier(0.45, 1.64, 0.47, 0.66);
  transform: scaleX(1);
}
.author-avatar {
  transition: all 0.5s;
}
.author-avatar:hover {
  transform: rotate(360deg);
}
.web-info {
  padding: 0.25rem;
  font-size: 0.875rem;
}
.scroll-down-effects {
  color: #eee !important;
  text-align: center;
  text-shadow: 0.1rem 0.1rem 0.2rem rgba(0, 0, 0, 0.15);
  line-height: 1.5;
  display: inline-block;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
  animation: scroll-down-effect 1.5s infinite;
}
@keyframes scroll-down-effect {
  0% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }
  50% {
    top: -16px;
    opacity: 1;
    filter: none;
  }
  100% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }
}
.big i {
  color: #f00;
  animation: big 0.8s linear infinite;
}
@keyframes big {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}
</style>
