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
@Component("/add_product")
@RequiredArgsConstructor
public class AddProductCommandExecutor implements CommandExecutor {

    private final BotUtils botUtils;
    private final BotKeyboardsCreator botKeyboardsCreator;

    @Override
    public SendMessage run(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initSavedProductsKeyboard();
            sendMessage.setText("CHOOSE\nВыберете продукт для добавления в корзину");
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            return sendMessage;
        } else {
            return botUtils.unauthMessegeExecution(sendMessage);
        }
    }

    @Override
    public String getCommand() {
        return "/add_product";
    }
}
