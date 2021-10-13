package ru.makar.makareshfamilybot.bot.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.makar.makareshfamilybot.bot.BotKeyboardsCreator;
import ru.makar.makareshfamilybot.bot.BotUtils;
import ru.makar.makareshfamilybot.web.FamilyWebService;

@Slf4j
@Component("default")
@RequiredArgsConstructor
public class SimpleMessageCommandExecutor implements CommandExecutor {

    private final BotUtils botUtils;
    private final FamilyWebService familyWebService;
    private final BotKeyboardsCreator botKeyboardsCreator;

    @Override
    public SendMessage run(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            String text = message.getText();
            if (text.startsWith("Комментарий:")) {
                String comment = text.replace("Комментарий:", "");
                sendMessage.setText("");
                InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initBasketProductsKeyboard();
                if (inlineKeyboardMarkup != null) {
                    sendMessage.setText("UPDATE COMMENT\nВыберете продукт у которого нужно изменить комментарий\n" + comment);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                } else {
                    sendMessage.setText("Лист покупок пуст...");
                }

            } else {
                InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initYesNoKeyboard();
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                sendMessage.setText(
                        String.format("SELECT\nДобавить этот продукт в список покупок ?\n%s",
                                text)
                );
            }
            return sendMessage;
        } else {
            return botUtils.unauthMessegeExecution(sendMessage);
        }
    }

    @Override
    public String getCommand() {
        return null;
    }
}
