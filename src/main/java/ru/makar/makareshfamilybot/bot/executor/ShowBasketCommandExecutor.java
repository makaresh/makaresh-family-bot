package ru.makar.makareshfamilybot.bot.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.makar.makareshfamilybot.bot.BotUtils;
import ru.makar.makareshfamilybot.model.BasketProduct;
import ru.makar.makareshfamilybot.web.FamilyWebService;

import java.util.List;

@Slf4j
@Component("/show_basket")
@RequiredArgsConstructor
public class ShowBasketCommandExecutor implements CommandExecutor{

    private final BotUtils botUtils;
    private final FamilyWebService familyWebService;

    @Override
    public SendMessage run(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            List<BasketProduct> basketList = familyWebService.getBasketList();
            if (!basketList.isEmpty()) {
                sendMessage.enableHtml(true);
                sendMessage.setParseMode(ParseMode.HTML);
                    String text = botUtils.formatedMessege(basketList);
                    sendMessage.setText(text);
            } else {
                    sendMessage.setText("Лист покупок пустой... \nМб пора что-нибудь добавить?");
            }
            return sendMessage;

        } else {
            return botUtils.unauthMessegeExecution(sendMessage);
        }
    }

    @Override
    public String getCommand() {
        return "/show_basket";
    }
}
