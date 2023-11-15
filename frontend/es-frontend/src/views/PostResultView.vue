<template>
  <div id="user_result_view">
    <a-table :data="props.postTable" stripe :pagination="false">
      <template #columns>
        <a-table-column title="title" :width="90">
          <template #cell="{ record }">
            {{ record.title }}
          </template>
        </a-table-column>
        <a-table-column title="tagList" :width="90">
          <template #cell="{ record }">
            <p v-for="(ta, index) of record.tagList" :key="index">{{ ta }}</p>
          </template>
        </a-table-column>
        <a-table-column title="content" :width="90">
          <template #cell="{ record }">
            {{ record.content }}
          </template>
        </a-table-column>
      </template>
    </a-table>
    <a-pagination
      :total="props.postCounts"
      show-total
      show-jumper
      show-page-size
      :page-size-options="pageSizes"
      @page-size-change="handleSizeChange"
      @change="handleCurrentChange"
      v-model:current="curPage"
      v-model:page-size="pageSize"
      style="
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        margin: 2% auto;
        padding: 0;
        justify-content: center;
        -webkit-justify-content: center;
      "
    >
      <template #page-item="{ page }"> - {{ page }} -</template>
      <template #page-item-step="{ type }">
        <icon-send
          :style="
            type === 'previous' ? { transform: `rotate(180deg)` } : undefined
          "
        />
      </template>
      <template #page-item-ellipsis>
        <icon-sun-fill />
      </template>
    </a-pagination>
  </div>
</template>

<script lang="ts" setup>
import { IconSend, IconSunFill } from "@arco-design/web-vue/es/icon";
import { defineProps, onMounted, ref, withDefaults } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const curPage = ref(1);
const pageSize = ref(8);
const pageSizes = ref([8, 16, 24]);

interface Props {
  searchContent: string;
  part: string;
  postTable: [];
  postCounts: number;
  getList: () => Promise<null>;
}

const props = withDefaults(defineProps<Props>(), {
  searchContent: () => "",
  part: () => "post",
  postTable: () => [],
  postCounts: () => 0,
  getList: async () => {
    return null;
  },
});
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  startNewSearch();
};
const handleCurrentChange = (val: number) => {
  curPage.value = val;
  startNewSearch();
};

const startNewSearch = async () => {
  await props.getList();
  await router.push({
    path: "/search",
    query: {
      part: props.part,
      curPage: curPage.value,
      pageSize: pageSize.value,
      searchContent: props.searchContent,
    },
  });
};

onMounted(() => {
  pageSize.value = parseInt(route.query.pageSize as string) || 8;
  curPage.value = parseInt(route.query.curPage as string) || 1;
  props.getList();
});
</script>

<style scoped>
#user_result_view {
  inset: 0;
  width: 100%;
}
</style>
