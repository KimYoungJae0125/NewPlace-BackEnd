package shop.newplace.users.token.model.entity;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@RedisHash("refreshToken")
public class JwtRefreshToken {
	
	private Long id;
	
	private String loginEmail;
	
	private String refreshToken;
	
	private Long expirationTime;
	
}
