package shop.newplace.common.config;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.RequiredArgsConstructor;
import shop.newplace.users.token.JwtTokenProvider;

@EnableJpaAuditing
@RequiredArgsConstructor
@Configuration
public class AuditorAwareConfig {

	private final HttpServletRequest request;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Bean
	public AuditorAware<String> auditorAware(){
		return new AuditorAware<>() {
			
			@Override
			public Optional<String> getCurrentAuditor() {
				String token = jwtTokenProvider.resolveAccessToken(request);
				String loginEmail = "adimn";
				if(token != null && jwtTokenProvider.validateToken(token)) {
					loginEmail = jwtTokenProvider.getLoginEmailByToken(token);
				}
				return Optional.of(loginEmail);
			}
			
		};
	}
	
}
