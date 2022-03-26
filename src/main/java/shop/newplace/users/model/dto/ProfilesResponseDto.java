package shop.newplace.users.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfilesResponseDto {
	
	@Getter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class Info{
		private String email;
		private String nickName;
		private String introduction;
		private String bankId;
		private String accountNumber;
		private String phoneNumber;
		private String authId;
	}
	
}
