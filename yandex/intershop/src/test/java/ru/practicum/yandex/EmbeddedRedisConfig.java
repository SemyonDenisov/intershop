package ru.practicum.yandex;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
@ActiveProfiles("test")
public class EmbeddedRedisConfig {

    @Bean(destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        var redisServer = new RedisServer();
        redisServer.start();
        return redisServer;
    }

}

