package shop.newplace.Users.token;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.service.CustomUserDetailService;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Value("spring.jwt.secret")
	private String SECRET_KEY;
	
	private long tokenValidMilisecond = 100L * 60 * 60; //1시간 토큰 유효
	
	private final CustomUserDetailService customUserDetailService;
	

	@PostConstruct
	protected void init() {
		SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	}
	
	public String createToken(String userPk, Collection<? extends GrantedAuthority> roles) {
		Claims claims = Jwts.claims().setSubject(userPk);
		claims.put("roles", roles);
		Date now = new Date();
		
		log.info("claims? : " + claims);
		
		return Jwts.builder()
					.setClaims(claims)	//데이터
					.setIssuedAt(now)	//토큰 발해일자
					.setExpiration(new Date(now.getTime() + tokenValidMilisecond))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY)	//암호화 알고리즘
					.compact();
	}
	
}
