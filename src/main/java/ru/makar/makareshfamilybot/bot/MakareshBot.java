package ru.makar.makareshfamilybot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.makar.makareshfamilybot.bot.callback.CallbackService;
import ru.makar.makareshfamilybot.bot.executor.CommandExecutor;
import ru.makar.makareshfamilybot.model.BasketProduct;
import ru.makar.makareshfamilybot.web.FamilyWebService;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakareshBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

    private final CallbackService callbackService;
    private final Map<String, CommandExecutor> commandExecutorMap;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String messageTextCommand = message.getText();
            SendMessage sendMessage;
            if (commandExecutorMap.containsKey(messageTextCommand)) {
                sendMessage = commandExecutorMap.get(messageTextCommand).run(message);
            } else {
                sendMessage = commandExecutorMap.get("default").run(message);
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                log.error("Can't send message...", ex);
            }
        }
        if (update.hasCallbackQuery()) {
            try {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                execute(callbackService.catchCallback(callbackQuery));
            } catch (TelegramApiException ex) {
                log.error("Callback is not callback...", ex);
            }
        }
    }
}
