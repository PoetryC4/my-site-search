import { StoreOptions } from "vuex";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { UserControllerService } from "@/api";

export default {
  namespaced: true,
  state: () => ({
    jwt: "",
    userInfo: {
      userAccount: null,
      userName: "未登录",
      id: -1,
      loginDate: new Date("1970-01-01T00:00:00"),
      userRole: roleEnum.NOT_LOGIN,
      isVip: 0,
      userAvatar: null,
      createTime: null,
      userProfile: null,
    },
  }),
  getters: {
    getJwt(state) {
      return state.jwt || null;
    },
  },
  actions: {
    async getLoginUser({ commit, state }, payload) {
      // id === -1 代表未登录
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 1) {
        commit("userLogin", res.data);
      } else {
        commit("userLogin", {
          ...state.userInfo,
          userRole: roleEnum.NOT_LOGIN,
          isVip: 0,
        });
      }
    },
    async logoutUser({ commit, state }) {
      commit("userLogout");
    },
    async jwtSetter({ commit, state }, payload) {
      commit("setJwt", payload);
    },
  },
  mutations: {
    userLogout(state) {
      state.userInfo.userName = "未登录";
      state.userInfo.id = -1;
      state.userInfo.loginDate = new Date("1970-01-01T00:00:00");
      state.userInfo.role = roleEnum.NOT_LOGIN;
      state.userInfo.isVip = 0;
      state.userInfo.createTime = null;
      state.userInfo.userAvatar = null;
      state.userInfo.userProfile = null;
      state.userInfo.userAccount = null;
      state.userInfo.jwt = "";
    },
    userLogin(state, payload) {
      state.userInfo = payload;
    },
    setJwt: (state, payload) => {
      state.jwt = payload.jwt;
    },
  },
} as StoreOptions<any>;
