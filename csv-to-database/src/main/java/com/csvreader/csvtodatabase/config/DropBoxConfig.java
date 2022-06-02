package com.csvreader.csvtodatabase.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DropBoxConfig {

    @Value("${dropbox.name}")
    private String name;
    @Value("${dropbox.accessToken}")
    private String accessToken;

    @Bean
    public DbxClientV2 createDbxClient() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(name).build();
        return new DbxClientV2(config, accessToken);
    }
}
