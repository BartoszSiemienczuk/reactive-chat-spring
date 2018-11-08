package com.softwarehut.reactivechat;

import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import(EmbeddedMongoAutoConfiguration.class)
public class EmbeddedMongoConfig {
}
