package com.authserver.adapter.out.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"local"})
@Configuration
public class LocalRedisConfig {

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//
//        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
//    }
}
