package shop.newplace.common.util;

import java.time.Duration;
<<<<<<< HEAD

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.model.dto.EmailDto;
=======
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.model.dto.EmailRequestDto;
>>>>>>> pre/feature/2022-03-06_signup
import shop.newplace.users.token.model.dto.JwtDto;

@Service
@RequiredArgsConstructor
public class RedisUtil {
	//2022-03-11 RedisService -> RedisUtil로 변경

	private final RedisTemplate<String, Object> redisTemplate;
	
	private final String CERTIFICATION_NUMBER_PREFIX = "certificationNumber.";
	
	private final String REFRESH_TOKEN_PREFIX = "refreshToken.";
	
//	public void setValues(String token, String email) {
//		ValueOperations<String, String> values = redisTemplate.opsForValue();
//		values.set(token, email, Duration.ofMinutes(3));
//	}

	/**
	 * set refreshToken
	 * @param refreshToken : { id : 회원의 Id, refreshToken : 리프레쉬 토큰, expirationTime : 만료 시간 }
	 */
	public void setValues(JwtDto.RefreshToken refreshToken) {
//		redisTemplate.opsForValue().set(refreshToken.getRefreshToken(), refreshToken.getLoginEmail(), Duration.ofMinutes(refreshToken.getExpirationTime()));
		//key, value, expiredTime
		redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + refreshToken.getId(), refreshToken.getRefreshToken(), Duration.ofMinutes(refreshToken.getExpirationTime()));
	}
	
	/**
	 * get refreshToken
	 * @param id : 회원의 id
	 * @return : refreshToken.{userId}의 refreshToken 값
	 */
	public String getValues(Long id) {
		try {
			return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + id).toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * delete refreshToken
	 * @param id : refreshToken.{userId}의 refershToken 삭제
	 */
	public void deleteValues(Long id) {
//		redisTemplate.delete(token.substring(7));
		redisTemplate.delete(REFRESH_TOKEN_PREFIX + id);
	}
	
	
	/**
	 * set certificationNumber
	 * @param emailDto : { loginEmail : 현재 회원가입하는 사용자의 이메일, certificationNumber : 인증번호, expirationTime : 인증번호 만료시간 }
	 */
	public void setValues(EmailRequestDto.EmailAuthentication emailDto) {
		redisTemplate.opsForValue().set(CERTIFICATION_NUMBER_PREFIX + emailDto.getLoginEmail(), emailDto.getCertificationNumber(), Duration.ofMinutes(emailDto.getExpirationTime()));
		
	}
	
	/**
	 * get certificationNumber
	 * @param loginEmail : certificationNumber.{loginEmail}의 인증번호 값
	 * @return
	 */
	public String getValues(String loginEmail) {
		try {
			return redisTemplate.opsForValue().get(CERTIFICATION_NUMBER_PREFIX + loginEmail).toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * delete certificationNumber
	 * @param loginEmail : certificationNumber.{loginEmail}의 인증번호를 지움
	 */
	public void deleteValues(String loginEmail) {
		redisTemplate.delete(CERTIFICATION_NUMBER_PREFIX + loginEmail);
	}
	
}
