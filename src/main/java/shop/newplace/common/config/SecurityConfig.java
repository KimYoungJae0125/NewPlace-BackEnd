package shop.newplace.common.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.security.CustomAccessDeniedHandler;
import shop.newplace.common.security.CustomAuthenticationEntryPoint;
import shop.newplace.common.security.CustomAuthenticationProvider;
import shop.newplace.common.security.CustomOnceRequestFilter;
import shop.newplace.common.security.CustomUserDetailsService;
import shop.newplace.users.repository.UsersRepository;
import shop.newplace.users.token.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailsService customUserDetailsService;

	private final JwtTokenProvider jwtTokenProvider;

	private final UsersRepository usersRepository;

	private final String[] USER_MVC_MATCHERS_PATTERNS = {
													"/jwt/**"
										 			};
	
	private final String[] ALL_MVC_MATCHERS_PATTERNS = {
													"/"
												   ,"/users/**"
													};

	private final String[] ANT_MATCHERS = {
											  "/h2-console/**"
											, "/swagger-ui/**"
											, "/swagger-resources/**"
											, "/v3/api-docs/**"
											, "/email/**"
//											, "/jwt/**"
											};

    @Override
    public void configure(WebSecurity web) throws Exception {
    	//PathRequest.toStaticResources().atCommonLocations() = static 리소스들의 기본위치를 모두 가져와 스프링 시큐리티에서 제외
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(ANT_MATCHERS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
				.cors()	
				.configurationSource(corsConfigurationSource())
        	.and()
	        	.httpBasic().disable()	//security 기본 로그인페이지 사용 안 함
	        	.csrf().disable()		//Rest API 사용으로 인해 csrf 사용 안 함
	        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)	//JWT 토큰 사용할것이므로  기존 시큐리티 세션 사용하지 않음
        	.and()
	        	.addFilterBefore(new CustomOnceRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) 
	        	/* 
	        	 * .addFilterBefore(new JwtBeforeFilter(jwtTokenProvider), SecurityContextPersistenceFilter.class)
	        	 * 2022-03-10 기존 SecurityContextPersistenceFilter => UsernamePasswordAuthenticationFilter 변경
	        	 * 2022-03-11 Class 명칭 JwtBeforeFilter -> CustomOnceRequestFilter로 변경
	        	 */
//	        	.addFilterAt(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
	        	.authorizeRequests()
	        	.mvcMatchers(HttpMethod.GET, USER_MVC_MATCHERS_PATTERNS).hasAnyRole("USER", "PARTNER")
	        	.mvcMatchers(HttpMethod.POST, USER_MVC_MATCHERS_PATTERNS).hasAnyRole("USER", "PARTNER")	//2022-03-09 permitAll -> hasRole로 변경 -> 2022-03-10 hasAnyRole로 변경
	        	.mvcMatchers(HttpMethod.POST, ALL_MVC_MATCHERS_PATTERNS).permitAll()
	        	.anyRequest().authenticated()
        	.and()
            	.exceptionHandling()
            	.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
		        .accessDeniedHandler(new CustomAccessDeniedHandler());	
        		/*
        		 * accessDeniedHandler
        		 * 권한이 없는 사용자
        		 * CustomOnceRequestFilter 검증 실패시 해당 로직 작동
        		 */
        
        
//        	.and()
//        		.formLogin()
//        			.loginPage("/users/login")
//        			.usernameParameter("loginEmail")
//        			.passwordParameter("password")
//        			.defaultSuccessUrl("/users/loginSuccess")
//        			.failureUrl("/users/loginFailure")
//        	.and()
//        		.logout()
//        			.logoutSuccessUrl("/")
//        			.invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(customUserDetailsService)
    		.passwordEncoder(getPasswordEncoder())
    		.and()
    		.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
    	return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
    	return new CustomAuthenticationProvider(customUserDetailsService, getPasswordEncoder(), usersRepository);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	// TODO Auto-generated method stub
    	return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration corsConfiguration = new CorsConfiguration();
    	corsConfiguration.addAllowedOrigin("http://localhost:3000");
    	corsConfiguration.addAllowedHeader("*");
    	corsConfiguration.addAllowedMethod("*");
    	corsConfiguration.setAllowCredentials(true);
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", corsConfiguration);
    	return source;
    }


}
