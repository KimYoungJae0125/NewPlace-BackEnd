package shop.newplace.Users.token.model.entity;

import java.time.LocalDateTime;

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
