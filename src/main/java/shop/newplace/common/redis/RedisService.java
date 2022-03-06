package shop.newplace.common.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.users.token.model.dto.JwtDto;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	
	private String prefix = "refreshToken.";
	
//	public void setValues(String token, String email) {
//		ValueOperations<String, String> values = redisTemplate.opsForValue();
//		values.set(token, email, Duration.ofMinutes(3));
//	}

	public void setValues(JwtDto.RefreshToken refreshToken) {
//		redisTemplate.opsForValue().set(refreshToken.getRefreshToken(), refreshToken.getLoginEmail(), Duration.ofMinutes(refreshToken.getExpirationTime()));
		//key, value, expiredTime
		redisTemplate.opsForValue().set(prefix + refreshToken.getId(), refreshToken.getRefreshToken(), Duration.ofMinutes(refreshToken.getExpirationTime()));
	}
	
	public String getValues(Long id) {
		return redisTemplate.opsForValue().get(prefix + id).toString();
	}
	
	public void deleteValues(Long id) {
//		redisTemplate.delete(token.substring(7));
		redisTemplate.delete(prefix + id);
	}
	
	
}
