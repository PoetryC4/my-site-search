import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import SearchView from "../views/SearchView.vue";
import { roleEnum } from "@/components/scripts/access/roleEnum";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/search",
    name: "search",
    component: SearchView,
  },
  {
    path: "/",
    redirect: "/search",
    meta: {
      access: roleEnum.NOT_LOGIN,
      hideInMenu: true,
    },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
