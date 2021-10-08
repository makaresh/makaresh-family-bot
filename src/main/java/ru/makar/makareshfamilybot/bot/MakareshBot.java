package ru.makar.makareshfamilybot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakareshBot extends TelegramLongPollingBot {


    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

    private final BotUtils botUtils;

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
        Message message = update.getMessage();
        String messageTextCommand = message.getText();
        if ("/add_user".equals(messageTextCommand)) {
            onAddNewUser(message);
        }
    }

    private void onAddNewUser(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (!botUtils.checkUser(message.getFrom().getId())) {
            Long id = message.getFrom().getId();
            String firstName = message.getFrom().getFirstName();
            String lastName = message.getFrom().getLastName();
            botUtils.addPotenialUser(id, firstName + " " + lastName);
            try {
                sendMessage.setText(
                        String.format(
                                "Обратитесь к разработчику для добавления вас в эту хрень, %s %s)))))",
                                firstName,
                                lastName));
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                log.error("Can't send message...", ex);
            }
        } else {
            try {
                sendMessage.setText("А вы уже тут есть, конгратс)))");
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                log.error("Can't send message...", ex);
            }
        }
    }

}
