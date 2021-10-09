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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.makar.makareshfamilybot.bot.callback.CallbackService;
import ru.makar.makareshfamilybot.model.BasketProduct;
import ru.makar.makareshfamilybot.web.FamilyWebService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakareshBot extends TelegramLongPollingBot {


    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

    private final BotUtils botUtils;
    private final CallbackService callbackService;
    private final FamilyWebService familyWebService;
    private final BotKeyboardsCreator botKeyboardsCreator;

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
            switch (messageTextCommand) {
                case "/show_basket":
                    onShowBasketList(message);
                    break;
                case "/add_product":
                    onAddProduct(message);
                    break;
                case "/add_user":
                    onAddNewUser(message);
                    break;
                case "/update_quantity":
                    onUpdateQuantity(message);
                    break;
                case "/":
                    break;
                default:
                    onSimpleMessage(message);
                    break;
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

    private void onAddProduct(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initSavedProductsKeyboard();
            try {
                sendMessage.setText("CHOOSE\nВыберете продукт для добавления в корзину");
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                log.error("Can't send message...", ex);
            }
        } else {
            unauthMessegeExecution(sendMessage);
        }
    }

    private void onShowBasketList(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            List<BasketProduct> basketList = familyWebService.getBasketList();
            if (!basketList.isEmpty()) {
                sendMessage.enableHtml(true);
                sendMessage.setParseMode(ParseMode.HTML);
                try {
                    String text = botUtils.formatedMessege(basketList);
                    sendMessage.setText(text);
                    execute(sendMessage);
                } catch (TelegramApiException ex) {
                    log.error("Can't send message...", ex);
                }
            } else {
                try {
                    sendMessage.setText("Лист покупок пустой... \nМб пора что-нибудь добавить?");
                    execute(sendMessage);
                } catch (TelegramApiException ex) {
                    log.error("Can't send message...", ex);
                }
            }

        } else {
            unauthMessegeExecution(sendMessage);
        }
    }

    private void onUpdateQuantity(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initBasketProductsKeyboard();
            if (inlineKeyboardMarkup != null) {
                try {
                    sendMessage.setText("UPDATE QUANTITY\nДавай изменим количество\nЕсли изменять в граммах, то жми +100, если в штуках, то +1");
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    execute(sendMessage);
                } catch (TelegramApiException ex) {
                    log.error("Can't send message...", ex);
                }
            } else {
                try {
                    sendMessage.setText("Лист покупок пуст...");
                    execute(sendMessage);
                } catch (TelegramApiException ex) {
                    log.error("Can't send message...", ex);
                }
            }
        } else {
            unauthMessegeExecution(sendMessage);
        }
    }

    private void onSimpleMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (botUtils.checkUser(message.getFrom().getId())) {
            try {
                InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initYesNoKeyboard();
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                sendMessage.setText(
                        String.format("SELECT\nДобавить этот продукт в список покупок ?\n%s",
                                message.getText())
                );
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                log.error("Can't send message...", ex);
            }
        } else {
            unauthMessegeExecution(sendMessage);
        }
    }

    private void unauthMessegeExecution(SendMessage sendMessage) {
        try {
            sendMessage.setText("А вы тут не авторизованы судддрь");
            execute(sendMessage);
        } catch (TelegramApiException ex) {
            log.error("Can't send message...", ex);
        }
    }
}
