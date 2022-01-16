package shop.newplace.common.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import shop.newplace.Users.service.UsersService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



	@Autowired
	private UsersService usersService;
	
	@Autowired
	private CustomAuthenticationProvider authProvider;
	
	private final String[] MVC_MATCHERS_PATTERNS = {
													"/"
										 		  , "users/*"
										 			};
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//Post 테스트용 실 운영에서는 제거해야함
    	http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, MVC_MATCHERS_PATTERNS).permitAll()
                .mvcMatchers(HttpMethod.POST, MVC_MATCHERS_PATTERNS).permitAll()
                .anyRequest().authenticated()
        	.and()
        		.formLogin()
        			.loginPage("/users/login")
        			.usernameParameter("loginEmail")
        			.passwordParameter("password")
        			.defaultSuccessUrl("/users/loginSuccess")
        			.failureUrl("/users/loginFailure")
        	.and()
        		.logout()
        			.logoutSuccessUrl("/")
        			.invalidateHttpSession(true);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(usersService)
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
