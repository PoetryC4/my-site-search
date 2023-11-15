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
            :selected-keys="[selectedKeys]"
            @menu-item-click="handleMenuClick"
          >
            <a-menu-item key="user">用户</a-menu-item>
            <a-menu-item key="post">评论</a-menu-item>
            <a-menu-item key="test">测试</a-menu-item>
          </a-menu>
        </a-layout-header>
        <a-layout-content style="margin-top: 30px">
          <UserResultView
            v-if="selectedKeys === 'user'"
            :part="part"
            :search-content="searchContent"
            :user-counts="data.userCounts"
            :user-table="data.userTable"
            :get-list="getUserList"
          />
          <PostResultView
            v-else-if="selectedKeys === 'post'"
            :part="part"
            :search-content="searchContent"
            :post-counts="data.postCounts"
            :post-table="data.postTable"
            :get-list="getPostList"
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
import { PostControllerService, UserControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";

const route = useRoute();
const router = useRouter();

const searchContent = ref("");
const curPage = ref(1);
const pageSize = ref(8);
const selectedKeys = ref("user");
const part = ref("");

const handleMenuClick = (key: string) => {
  selectedKeys.value = key;
  part.value = key;
  doSearch();
};

const data = reactive({
  postTable: [],
  postCounts: 0,
  userTable: [],
  userCounts: 0,
});

const getPostList = async () => {
  const res = await PostControllerService.listPostVoByPageUsingPost({
    current: curPage.value,
    pageSize: pageSize.value,
    content:
      searchContent.value.length === undefined || searchContent.value.length < 1
        ? null
        : searchContent.value,
  });
  if (res.code != 0) {
    Message.error("err" + res.message);
  } else {
    data.postTable = res.data.records || [];
    data.postCounts = parseInt(res.data.total);
  }
  return null;
};

const getUserList = async () => {
  const res = await UserControllerService.listUserVoByPageUsingPost({
    current: curPage.value,
    pageSize: pageSize.value,
    userName:
      searchContent.value.length === undefined || searchContent.value.length < 1
        ? null
        : searchContent.value,
  });
  if (res.code != 0) {
    Message.error("err" + res.message);
  } else {
    data.userTable = res.data.records || [];
    data.userCounts = parseInt(res.data.total);
  }
  return null;
};

const doSearch = async () => {
  switch (part.value) {
    default: {
      await getUserList();
      break;
    }
    case "post": {
      await getPostList();
      break;
    }
  }
  await router.push({
    path: "/search",
    query: {
      part: part.value,
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
  part.value = (route.query.part as string) || "";
  // 记忆化
  if (part.value === "user") {
    selectedKeys.value = "user";
  } else if (part.value === "post") {
    selectedKeys.value = "post";
  } else if (part.value === "test") {
    selectedKeys.value = "test";
  } else {
    selectedKeys.value = "user";
  }
});
</script>
