package ru.makar.makareshfamilybot.bot.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.makar.makareshfamilybot.bot.BotUtils;

@Slf4j
@Component("/add_user")
@RequiredArgsConstructor
public class AddUserCommandExecutor implements CommandExecutor {

    private final BotUtils botUtils;

    @Override
    public SendMessage run(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (!botUtils.checkUser(message.getFrom().getId())) {
            Long id = message.getFrom().getId();
            String firstName = message.getFrom().getFirstName();
            String lastName = message.getFrom().getLastName();
            botUtils.addPotenialUser(id, firstName + " " + lastName);
            sendMessage.setText(
                    String.format(
                            "Обратитесь к разработчику для добавления вас в эту хрень, %s %s)))))",
                            firstName,
                            lastName));
        } else {
            sendMessage.setText("А вы уже тут есть, конгратс)))");
        }
        return sendMessage;
    }

    @Override
    public String getCommand() {
        return "/add_user";
    }
}
