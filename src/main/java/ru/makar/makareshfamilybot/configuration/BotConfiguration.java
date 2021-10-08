package ru.makar.makareshfamilybot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class BotConfiguration {

    @Bean
    public List<Long> usersList() {
        return List.of(304628032L);
    }

    @Bean
    public Set<Map<Long, String>> potentialUsers() {
        return new HashSet<>();
    }
}
