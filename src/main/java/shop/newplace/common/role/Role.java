package shop.newplace.common.role;

import lombok.Getter;

@Getter
public enum Role {
	  USER("ROLE_USER")
	, PARTNER("ROLE_PARTNER")
	, ADMIN("ROLE_ADMIN");

	private String roleValue;
	
	Role(String roleValue) {
		this.roleValue = roleValue;
	}
	

}
