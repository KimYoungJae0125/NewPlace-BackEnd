package shop.newplace.common.model.dto;

import shop.newplace.users.token.model.dto.JwtDto;

public class JwtTestDto {
	
	private final Long EXPIRTATION_TIME = 100L * 60 * 60 * 24 * 7;
	
	public JwtDto.RefreshToken createRefreshToken(Long id, String refreshToken) {
		return JwtDto.RefreshToken.builder()
								  .id(id)
								  .expirationTime(EXPIRTATION_TIME)
								  .refreshToken(refreshToken)
								  .build();
	}

}
