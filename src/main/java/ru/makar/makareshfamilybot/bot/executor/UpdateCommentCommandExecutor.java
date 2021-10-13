package ru.makar.makareshfamilybot.bot.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.makar.makareshfamilybot.bot.BotUtils;

@Slf4j
@Component("/update_comment")
@RequiredArgsConstructor
public class UpdateCommentCommandExecutor implements CommandExecutor {

    private final BotUtils botUtils;

    @Override
    public SendMessage run(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            sendMessage.setText("UPDATE COMMENT\nСначала напишите комментарий, в начале сообщения используйте \"комментарий:\", после этого выберете продукт к которому этот комментарий относился");
            return sendMessage;
        } else {
            return botUtils.unauthMessegeExecution(sendMessage);
        }
    }

    @Override
    public String getCommand() {
        return "/update_comment";
    }
}
