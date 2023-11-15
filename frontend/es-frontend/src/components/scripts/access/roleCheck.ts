import { roleEnum } from "@/components/scripts/access/roleEnum";

export const roleChecker = (inputUser: any, targetRole: string) => {
  if (targetRole === roleEnum.NOT_LOGIN) return true;
  else if (targetRole === roleEnum.DEFAULT_USER) {
    return (
      inputUser?.userRole === roleEnum.DEFAULT_USER ||
      inputUser?.userRole === roleEnum.ADMIN
    );
  } else if (targetRole === roleEnum.ADMIN) {
    return inputUser?.userRole === roleEnum.ADMIN;
  } else return false;
};
