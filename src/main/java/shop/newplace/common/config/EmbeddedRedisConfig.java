package shop.newplace.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import lombok.RequiredArgsConstructor;
import redis.embedded.RedisServer;

@RequiredArgsConstructor
@Configuration
@Profile({"local", "test"})
public class EmbeddedRedisConfig {

    private final RedisProperties redisProperties;
    
    private static final String BIN_SH = "/bin/sh";
    private static final String BIN_SH_OPTION = "-c";
    private static final String COMMAND = "netstat -nat | grep LISTEN|grep %d";

    
    private RedisServer redisServer;
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }
    
    @PostConstruct
    public void redisServer() throws IOException {
    	int redisPort = isRedisRunning() ? findAvailablePort() : redisProperties.getPort();
        redisServer = RedisServer.builder()
	            				 .port(redisPort)
	            				 .setting("maxmemory 128M")
	            				 .build();
    	redisServer.start();
    }
    
    @PreDestroy
    public void stopRedis() {
        if(redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisProperties.getPort()));
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return !pidInfo.toString().isEmpty();
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format(COMMAND, port);
        String[] shell = {BIN_SH, BIN_SH_OPTION, command};
        return Runtime.getRuntime().exec(shell);
    }

    public int findAvailablePort() throws IOException {

        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }
    
}
