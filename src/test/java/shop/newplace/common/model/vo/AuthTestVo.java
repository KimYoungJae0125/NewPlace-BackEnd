package shop.newplace.common.model.vo;

import lombok.Getter;
import shop.newplace.common.constant.Role;

@Getter
public class AuthTestVo {
	private String adminAuthId = Role.ADMIN.getValue();
	private String userAuthId = Role.USER.getValue();
	private String partnerAuthId = Role.PARTNER.getValue();
}
