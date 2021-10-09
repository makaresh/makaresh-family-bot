package ru.makar.makareshfamilybot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.makar.makareshfamilybot.bot.MakareshBot;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class MakareshFamilyBotApplication {

    public static void main(String[] args) {
//        ApiContextInitializer.init();
        SpringApplication.run(MakareshFamilyBotApplication.class, args);
    }

}
