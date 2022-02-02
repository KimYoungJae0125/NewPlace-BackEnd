package shop.newplace.common.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.service.CustomUserDetailService;
import shop.newplace.Users.token.CustomAuthenticationEntryPoint;
import shop.newplace.Users.token.JwtAuthenticationFilter;
import shop.newplace.Users.token.JwtBeforeFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailService customUserDetailService;
	
	private final CustomAuthenticationProvider authProvider;
	
	private final String[] MVC_MATCHERS_PATTERNS = {
													"/"
												  , "/swagger-ui/**"
												  , "/swagger-resources/**"
												  , "/v3/api-docs"
												  , "/users"
										 		  , "users/**"
										 			};
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()	//security 기본 로그인페이지 사용 안 함
        	.csrf().disable()		//Rest API 사용으로 인해 csrf 사용 안 함
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)	//JWT인증 사용할것이므로 세션을 사용 함
        	.and()
            	.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
            	.addFilterBefore(new JwtBeforeFilter(), SecurityContextPersistenceFilter.class)
            	.addFilter(new JwtAuthenticationFilter(authenticationManager()))
            	.authorizeRequests()
            	.mvcMatchers(HttpMethod.GET, MVC_MATCHERS_PATTERNS).permitAll()
            	.mvcMatchers(HttpMethod.POST, MVC_MATCHERS_PATTERNS).permitAll()
            	.anyRequest().authenticated()
        	.and()
//        		.formLogin()
//        			.loginPage("/users/login")
//        			.usernameParameter("loginEmail")
//        			.passwordParameter("password")
//        			.defaultSuccessUrl("/users/loginSuccess")
//        			.failureUrl("/users/loginFailure")
//        	.and()
        		.logout()
        			.logoutSuccessUrl("/")
        			.invalidateHttpSession(true);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(customUserDetailService)
    		.passwordEncoder(getPasswordEncoder())
    		.and()
    		.authenticationProvider(authProvider);
    }
    
    @Bean
    public PasswordEncoder getPasswordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	// TODO Auto-generated method stub
    	return super.authenticationManagerBean();
    }
    
    

}
