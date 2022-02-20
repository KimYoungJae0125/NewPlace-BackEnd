package shop.newplace.Users.token;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.token.model.repository.JwtRefreshTokenRedisRepository;
import shop.newplace.common.redis.service.RedisService;
import shop.newplace.common.security.CustomUserDetailsService;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Value("spring.jwt.secret")
	private String SECRET_KEY;
	
	private long accesTokenValidMilisecond = 100L * 60 * 60; //1시간 토큰 유효
	private long refreshTokenValidMilisecond = 100L * 60 * 60 * 24 * 7; //일주일 토큰 유효
	
	private final CustomUserDetailsService customUserDetailsService;
	
	private final RedisService redisService;
	private final JwtRefreshTokenRedisRepository jwtRefreshTokenRedisRepository;
	
	private String type = "bearer ";
	

	@PostConstruct
	protected void init() {
		SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	}
	
	private String createToken(String loginEmail, Collection<? extends GrantedAuthority> roles, long tokenValidMilisecond) {
		Claims claims = Jwts.claims().setSubject(loginEmail);
		claims.put("roles", roles);
		Date now = new Date();
		
		log.info("claims? : " + claims);
		
		return Jwts.builder()
					.setClaims(claims)	//데이터
					.setIssuedAt(now)	//토큰 발행일자
					.setExpiration(new Date(now.getTime() + tokenValidMilisecond))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY)	//암호화 알고리즘
					.compact();
	}
	
	public Authentication getAuthentication(String token) {
		Users users = customUserDetailsService.loadUserByUsername(getLoginEmailByToken(token));
		return new UsernamePasswordAuthenticationToken(users, "", users.getAuthorities());
	}
	
	public String createAccessToken(String loginEmail, Collection<? extends GrantedAuthority> roles) {
		return createToken(loginEmail, roles, accesTokenValidMilisecond);
	}

	public String createRefreshToken(String loginEmail, Collection<? extends GrantedAuthority> roles) {
		return createToken(loginEmail, roles, refreshTokenValidMilisecond);
	}
	
	public String getLoginEmailByToken(String token) {
		return parserJwts(token)
				.getBody()
				.getSubject();
	}
	
	public String resolveAccessToken(HttpServletRequest request) {
		return resolveToken(request.getHeader("Authorization"));
	}

	public String resolveRefreshToken(HttpServletRequest request) {
		return resolveToken(request.getHeader("refreshToken"));
	}
	
	private String resolveToken(String token) {
		if(token != null) {
			return token.substring(7);
		} 
		return null;
	}
	
	public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader("authorization", type + accessToken);
	}

	public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
		response.setHeader("refreshToken", type + refreshToken);
	}
	
	public boolean existsRefreshTokenByLoginEmail(String loginEmail) {
		return redisService.getValues(loginEmail) != null;
//		return jwtRefreshTokenRedisRepository.existsByRefreshToken(refreshToken);
	}
	
	public boolean validateToken(String token) {
		try {
			return !parserJwts(token).getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
	
	private Jws<Claims> parserJwts(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
	}
	
	
} 