package shop.newplace.common.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class TymeleafConfig {
	
//	@Bean
//	public TemplateEngine htmlTemplateEngine() {
//		TemplateEngine templateEngine = new SpringTemplateEngine();
//		templateEngine.addTemplateResolver(springResourceTemplateResolver());
//		return templateEngine;
//	}

	@Bean
	@Primary
	public SpringTemplateEngine thymeleafTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setTemplateEngineMessageSource(emailMessageSource());
		return templateEngine;
	}

	@Bean
	public ITemplateResolver templateResolver() {
//		SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
//		springResourceTemplateResolver.setOrder(1);
		ClassLoaderTemplateResolver teamplteResolver = new ClassLoaderTemplateResolver();
		teamplteResolver.setPrefix("/templates/");
		teamplteResolver.setSuffix(".html");
		teamplteResolver.setTemplateMode(TemplateMode.HTML);
		teamplteResolver.setCharacterEncoding("UTF-8");
		teamplteResolver.setCacheable(false);
		
		return teamplteResolver;
	}
	
	public ResourceBundleMessageSource emailMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("mailMessages");
		return messageSource;
	}
	
}
