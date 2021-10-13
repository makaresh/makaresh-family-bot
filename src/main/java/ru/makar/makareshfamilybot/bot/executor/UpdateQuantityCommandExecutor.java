package ru.makar.makareshfamilybot.bot.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.makar.makareshfamilybot.bot.BotKeyboardsCreator;
import ru.makar.makareshfamilybot.bot.BotUtils;

@Slf4j
@Component("/update_quantity")
@RequiredArgsConstructor
public class UpdateQuantityCommandExecutor implements CommandExecutor{

    private final BotUtils botUtils;
    private final BotKeyboardsCreator botKeyboardsCreator;

    @Override
    public SendMessage run(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initBasketProductsKeyboard();
            if (inlineKeyboardMarkup != null) {
                    sendMessage.setText("UPDATE QUANTITY\nДавай изменим количество\nЕсли изменять в граммах, то жми +100, если в штуках, то +1");
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            } else {
                    sendMessage.setText("Лист покупок пуст...");
            }
            return sendMessage;
        } else {
            return botUtils.unauthMessegeExecution(sendMessage);
        }
    }

    @Override
    public String getCommand() {
        return "/update_quantity";
    }
}
