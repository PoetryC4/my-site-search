<template>
  <div id="search_view">
    <a-layout style="height: 400px">
      <a-layout-header style="width: 100%; height: 100px">
        <a-input
          style="
            width: 30%;
            inset: 0;
            float: right;
            margin-right: 100px;
            margin-left: auto;
            margin-top: 30px;
          "
          placeholder="搜索"
          allow-clear
          v-model="searchContent"
          @click="doSearch"
          @press-enter="doSearch"
        >
          <template #append>
            <a-button type="primary" @click="doSearch">
              <icon-search />
              搜索
            </a-button>
          </template>
        </a-input>
      </a-layout-header>
      <a-layout style="inset: 0; margin-left: 10%; margin-right: 10%">
        <a-layout-header>
          <a-menu
            mode="horizontal"
            :selected-keys="[category]"
            @menu-item-click="handleMenuClick"
          >
            <a-menu-item key="user">用户</a-menu-item>
            <a-menu-item key="post">评论</a-menu-item>
            <a-menu-item key="test">测试</a-menu-item>
          </a-menu>
        </a-layout-header>
        <a-layout-content style="margin-top: 30px">
          <UserResultView
            v-if="category === 'user'"
            :part="category"
            :search-content="searchContent"
            :user-counts="data.resultCounts"
            :user-table="data.resultTable"
            :get-list="getSearchList"
          />
          <PostResultView
            v-else-if="category === 'post'"
            :part="category"
            :search-content="searchContent"
            :post-counts="data.resultCounts"
            :post-table="data.resultTable"
            :get-list="getSearchList"
          />
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>

<script lang="ts" setup>
import { IconSearch } from "@arco-design/web-vue/es/icon";
import { onMounted, reactive, ref } from "vue";
import UserResultView from "@/views/UserResultView.vue";
import { useRoute, useRouter } from "vue-router";
import PostResultView from "@/views/PostResultView.vue";
import { SearchControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";

const route = useRoute();
const router = useRouter();

const searchContent = ref("");
const curPage = ref(1);
const pageSize = ref(8);
const category = ref("");

const handleMenuClick = (key: string) => {
  category.value = key;
  doSearch();
};

const data = reactive({
  resultTable: [],
  resultCounts: 0,
});

const getSearchList = async () => {
  const res = await SearchControllerService.listObjectByPageUsingPost({
    current: curPage.value,
    pageSize: pageSize.value,
    searchText:
      searchContent.value.length === undefined || searchContent.value.length < 1
        ? null
        : searchContent.value,
    category: category.value,
    userEs: true,
  });
  if (res.code != 0) {
    Message.error("err" + res.message);
  } else {
    data.resultTable = res.data.records || [];
    data.resultCounts = parseInt(res.data.total);
  }
  return null;
};

const doSearch = async () => {
  await getSearchList();
  await router.push({
    path: "/search",
    query: {
      category: category.value,
      curPage: curPage.value,
      pageSize: pageSize.value,
      searchContent: searchContent.value,
    },
  });
};
onMounted(() => {
  pageSize.value = parseInt(route.query.pageSize as string) || 8;
  curPage.value = parseInt(route.query.curPage as string) || 1;
  searchContent.value = (route.query.searchContent as string) || "";
  category.value = (route.query.category as string) || "user";
  doSearch();
});
</script>
