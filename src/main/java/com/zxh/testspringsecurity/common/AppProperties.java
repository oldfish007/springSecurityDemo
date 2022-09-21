package com.zxh.testspringsecurity.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

@Configuration
@ConfigurationProperties(prefix = "mooc")
public class AppProperties {

    @Getter
    @Setter
    private RedisPre redisPre = new RedisPre();

    @Getter
    @Setter
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class RedisPre{

        private  String REDIS_LOGIN_UID="login:";
    }
    @Getter
    @Setter
    public static class Jwt{

        private String header="Authorization";
        private String prefix="Bearer ";
    }
}
