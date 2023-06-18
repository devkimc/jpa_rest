package jparest.practice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "jparest.practice.auth.jwt")
public class RedisConfig {
}
