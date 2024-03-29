package shop.newplace.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {
	
	   private final RedisProperties redisProperties;
	    
	    @Bean
	    @ConditionalOnMissingBean(RedisConnectionFactory.class) 
	    public RedisConnectionFactory redisConnectionFactory() {
	        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//	        config.setPassword(redisProperties.getPassword());
//	      return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
	        return new LettuceConnectionFactory(config);
	    }

	    @Bean
	    @Primary
	    public RedisTemplate<String, Object> redisTemplate() {
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	        redisTemplate.setConnectionFactory(redisConnectionFactory());
	        redisTemplate.setKeySerializer(new StringRedisSerializer());
	        redisTemplate.setValueSerializer(new StringRedisSerializer());
	        return redisTemplate;
	    }
	    
	
}
