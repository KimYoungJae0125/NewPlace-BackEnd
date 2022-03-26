package shop.newplace.users.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UsersResponseDto {
	
	@Getter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class Info {
		private Long userId;
		private String loginEmail;
		private String name;
		private String bankId;
		private String accountNumber;
		private String mainPhoneNumber;
	}
	
}
