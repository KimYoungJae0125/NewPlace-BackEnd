package shop.newplace.common.mail.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailResponseDto {
	
	@Getter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class Info {
		
		private String certificationNumber;
		
		private boolean emailVerified;
	}
	
}
