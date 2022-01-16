package shop.newplace.common.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import shop.newplace.Users.model.entity.Users;

@EnableJpaAuditing
@Configuration
public class AuditorAwareConfig {

	private Users users;
	
	@Bean
	public AuditorAware<String> auditorAware(){
		return new AuditorAware<>() {
			
			@Override
			public Optional<String> getCurrentAuditor() {
				String loginEmail = "admin";
				return Optional.of(loginEmail);
			}
			
		};
	}
	
}
