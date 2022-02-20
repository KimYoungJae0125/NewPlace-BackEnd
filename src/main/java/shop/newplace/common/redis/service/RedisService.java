package shop.newplace.common.redis.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.token.model.entity.JwtRefreshToken;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	
//	public void setValues(String token, String email) {
//		ValueOperations<String, String> values = redisTemplate.opsForValue();
//		values.set(token, email, Duration.ofMinutes(3));
//	}

	public void setValues(JwtRefreshToken refreshToken) {
//		redisTemplate.opsForValue().set(refreshToken.getRefreshToken(), refreshToken.getLoginEmail(), Duration.ofMinutes(refreshToken.getExpirationTime()));
		redisTemplate.opsForValue().set(refreshToken.getLoginEmail(), refreshToken.getRefreshToken(), Duration.ofMinutes(refreshToken.getExpirationTime()));
	}
	
	public String getValues(String loginEmail) {
		return redisTemplate.opsForValue().get(loginEmail).toString();
	}
	
	public void deleteValues(String loginEmail) {
//		redisTemplate.delete(token.substring(7));
		redisTemplate.delete(loginEmail);
	}
	
	
}
