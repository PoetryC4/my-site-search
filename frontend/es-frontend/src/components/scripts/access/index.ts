import router from "@/router";
import store from "@/store";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { roleChecker } from "@/components/scripts/access/roleCheck";

router.beforeEach(async (to, from, next) => {
  const curUser = store.state.user?.userInfo;
  if (!curUser || !curUser.userRole || curUser.id === -1) {
    await store.dispatch("user/getLoginUser");
  }
  if (
    to.fullPath.startsWith("/user/login") ||
    to.fullPath.startsWith("/user/register")
  ) {
    if (store.state.user?.userInfo?.userRole !== roleEnum.NOT_LOGIN) {
      next("/user");
      return;
    }
  }
  if (!roleChecker(store.state.user?.userInfo, to.meta?.access as string)) {
    if (to.meta?.access === roleEnum.ADMIN) {
      if (store.state.user?.userInfo?.userRole === roleEnum.DEFAULT_USER) {
        next("/noAuth");
      } else if (store.state.user?.userInfo?.userRole === roleEnum.NOT_LOGIN) {
        next(`/user/login?redirect=${to.fullPath}`);
      }
      return;
    } else if (to.meta?.access === roleEnum.DEFAULT_USER) {
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    } else next();
  } else next();
});
