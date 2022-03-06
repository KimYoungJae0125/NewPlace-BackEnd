package shop.newplace.users.token.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class JwtDto {

	@Getter
	@Setter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class AccessToken{
		private Long id;
		
		private String accesToken;
	}
	
	@Getter
	@Setter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class RefreshToken{
		private Long id;

		private String refreshToken;
		
		private Long expirationTime;
	}
	
    
    
}
