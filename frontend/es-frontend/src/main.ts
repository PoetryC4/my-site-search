import { createApp } from "vue";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import App from "./App.vue";
import router from "./router";
import "@/components/scripts/access";
import store from "./store";

createApp(App).use(ArcoVue).use(store).use(router).mount("#app");

const app = createApp(App);
