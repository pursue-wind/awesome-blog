<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="openModel(null)"
      >新增
      </el-button>
      <el-button
          type="danger"
          size="small"
          icon="el-icon-delete"
          :disabled="this.categoryIdList.length == 0"
          @click="isDelete = true"
      >批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-input
            v-model="keywords"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入分类名"
            style="width:200px"
            @keyup.enter.native="searchCategories"
        />
        <el-button
            type="primary"
            size="small"
            icon="el-icon-search"
            style="margin-left:1rem"
            @click="searchCategories"
        >搜索
        </el-button>
      </div>
    </div>
    <el-input
        placeholder="输入关键字进行过滤"
        v-model="filterText">
    </el-input>

    <div>
      <el-tree
          ref="tree"
          :filter-node-method="filterNode"
          :data="categoryData"
          node-key="id"
          default-expand-all
          @node-drag-start="handleDragStart"
          @node-drag-enter="handleDragEnter"
          @node-drag-leave="handleDragLeave"
          @node-drag-over="handleDragOver"
          @node-drag-end="handleDragEnd"
          @node-drop="handleDrop"
          draggable
          :allow-drop="allowDrop"
          :allow-drag="allowDrag">
        <span class="custom-tree-node" slot-scope="{ node, data }">
            <div class="custom-tree">{{ node.label }} - {{node.label.count}}</div>
          <div>
            <el-button
                icon="el-icon-edit"
                type="text"
                size="mini"
                @click="() => update(data)">
            </el-button>
            <el-button
                icon="el-icon-delete"
                type="text"
                size="mini"
                @click="() => deleteCategory(node.id)">
            </el-button>
          </div>
        </span>
      </el-tree>
    </div>


  </el-card>
</template>

<script>
export default {
  created() {
    this.listCategories();
    this.listCategoriesTree();

    this.listArticles();
    this.listCategories();
    this.listTags();
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },

  data: function () {
    return {
      filterText: '',
      isDelete: false,
      loading: true,
      addOrEdit: false,
      keywords: null,
      categoryIdList: [],
      categoryList: [],
      categoryForm: {
        id: null,
        categoryName: "",
        parentId: ""
      },
      current: 1,
      size: 10,
      count: 0,

      categoryData: [],
    };
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.label.name.indexOf(value) !== -1;
    },
    selectionChange(categoryList) {
      this.categoryIdList = [];
      categoryList.forEach(item => {
        this.categoryIdList.push(item.id);
      });
    },
    searchCategories() {
      this.current = 1;
      this.listCategories();
    },
    sizeChange(size) {
      this.size = size;
      this.listCategories();
    },
    currentChange(current) {
      this.current = current;
      this.listCategories();
    },
    deleteCategory(id) {
      debugger
      var param = {};
      if (id == null) {
        param = {data: this.categoryIdList};
      } else {
        param = [id];
      }
      this.axios.delete("/api/admin/categories", param).then(({data}) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listCategories();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.isDelete = false;
      });
    },

    transformCategoriesDate(categories) {
      return categories.map(c => {
        return {
          id: c.id,
          label: {name: c.categoryName, count: c.articleCount, parentId: c.parentId},
          children: this.transformCategoriesDate(c.children === null ? [] : c.children)
        }
      })
    },
    listCategoriesTree() {
      this.axios
          .get("/api/admin/categories/tree")
          .then(({data}) => {
            this.categoryData = this.transformCategoriesDate(data.data)
            this.loading = false;
          });
    },
    openModel(category) {
      if (category != null) {
        this.categoryForm = JSON.parse(JSON.stringify(category));
        this.$refs.categoryTitle.innerHTML = "修改分类";
      } else {
        this.categoryForm.id = null;
        this.categoryForm.categoryName = "";
        this.categoryForm.parentId = "";
        this.$refs.categoryTitle.innerHTML = "添加分类";
      }
      this.addOrEdit = true;
    },
    addOrEditCategory() {
      if (this.categoryForm.categoryName.trim() == "") {
        this.$message.error("分类名不能为空");
        return false;
      }
      this.axios
          .post("/api/admin/categories", this.categoryForm)
          .then(({data}) => {
            if (data.flag) {
              this.$notify.success({
                title: "成功",
                message: data.message
              });
              this.listCategories();
            } else {
              this.$notify.error({
                title: "失败",
                message: data.message
              });
            }
            this.addOrEdit = false;
          });
    },
    listArticles() {
      this.axios
          .get("/api/admin/articles", {
            params: {
              current: this.current,
              size: this.size,
              keywords: this.keywords,
              categoryId: this.categoryId,
              status: this.status,
              tagId: this.tagId,
              type: this.type,
              isDelete: this.isDelete
            }
          })
          .then(({data}) => {
            this.articleList = data.data.recordList;
            this.count = data.data.count;
            this.loading = false;
          });
    },

    listCategories() {
      this.axios.get("/api/admin/categories/search").then(({data}) => {
        this.categoryList = data.data;
      });
    },
    listTags() {
      this.axios.get("/api/admin/tags/search").then(({data}) => {
        this.tagList = data.data;
      });
    },

    handleDragStart(node, ev) {
      console.log('drag start', node);
    },
    handleDragEnter(draggingNode, dropNode, ev) {
      console.log('tree drag enter: ', dropNode.label);
    },
    handleDragLeave(draggingNode, dropNode, ev) {
      console.log('tree drag leave: ', dropNode.label);
    },
    handleDragOver(draggingNode, dropNode, ev) {
    },
    handleDragEnd(draggingNode, dropNode, dropType, ev) {
      console.log('tree drag end: ', dropNode && dropNode.label, dropType);
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log('tree drop: ', dropNode.label, dropType);
    },
    allowDrop(draggingNode, dropNode, type) {
      if (dropNode.data.label.name === '书') {
        return false;
      } else {
        return true;
      }
    },
    allowDrag(draggingNode) {
      return draggingNode.data.label.parentId !== 0;
    }

  },
};

</script>
<style scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.custom-tree{
  width: 500px;
}
</style>
