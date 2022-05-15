package shop.newplace.common.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import lombok.RequiredArgsConstructor;
import redis.embedded.RedisServer;

@RequiredArgsConstructor
@Configuration
@Profile({"local", "test"})
public class EmbeddedRedisConfig {

    private final RedisProperties redisProperties;
    
    private RedisServer redisServer;
    
    @PostConstruct
    public void redisServer() {
        redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
    }
    
    @PreDestroy
    public void stopRedis() {
        if(redisServer != null) {
            redisServer.stop();
        }
    }

}
