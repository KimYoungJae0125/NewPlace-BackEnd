package shop.newplace.Users.model.entity;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@RedisHash("jwtRefreshToken")
public class JwtRefreshToken {
	
	private Long id;
	
	private String refreshToken;
	
}
