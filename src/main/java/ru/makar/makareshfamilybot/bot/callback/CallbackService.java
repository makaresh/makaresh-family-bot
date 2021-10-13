package ru.makar.makareshfamilybot.bot.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.makar.makareshfamilybot.bot.BotKeyboardsCreator;
import ru.makar.makareshfamilybot.web.FamilyWebService;

@Service
@RequiredArgsConstructor
public class CallbackService {

    private final FamilyWebService familyWebService;
    private final BotKeyboardsCreator botKeyboardsCreator;

    public SendMessage catchCallback(CallbackQuery callbackQuery) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
        String text = callbackQuery.getMessage().getText();
        String[] split = text.split(String.valueOf('\n'));
        String title = split[0];
        String selectedData = callbackQuery.getData();
        switch (title) {
            case "CHOOSE":
                if ("Другой".equals(selectedData)) {
                    sendMessage.setText("Напишите название продукта");
                } else {
                    sendMessage.setText("%s уже в корзине, если хотите, то можете изменить его количество");
                }
                break;
            case "SELECT":
                if ("Yes".equals(selectedData)) {
                    addProduct(sendMessage, split[2]);
                } else {
                    sendMessage.setText("OK. Не добавлю...");
                }
                break;
            case "UPDATE COMMENT":
                updateComment(sendMessage, selectedData, split[2]);
                break;
            case "UPDATE QUANTITY":
                onUpdateQuantity(sendMessage, selectedData);
                break;
            case "UPDATER QUANTITY":
                updateQuantity(sendMessage, split[1], selectedData);
                break;
            case "EMPTY BASKET":
                if ("Yes".equals(selectedData)) {
                    emptyBasket(sendMessage);
                } else {
                    sendMessage.setText("OK. Не добавлю...");
                }
                break;
        }
        return sendMessage;
    }

    private void addProduct(SendMessage sendMessage, String selectedData) {
        ResponseEntity<Void> voidResponseEntity = familyWebService.addProductToBasket(selectedData);
        if (voidResponseEntity.getStatusCode().is2xxSuccessful()) {
            sendMessage.setText(String.format("Продукт %s был добавлен в корзину", selectedData));
        } else {
            sendMessage.setText("Произошла ошибка...");
        }
    }

    private void onUpdateQuantity(SendMessage sendMessage, String selectedData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = botKeyboardsCreator.initQuantityKeyboard();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(String.format("UPDATER QUANTITY\nВыберете количество на которое нужно изменить продукт %s", selectedData));
    }

    private void updateQuantity(SendMessage sendMessage, String product, String summand) {
        String sign;
        if (summand.startsWith("-")) {
            sign = "-";
        } else {
            sign = "+";
        }
        String number = summand.substring(1);
        String text = familyWebService.updateProductQuantity(product, Double.valueOf(number), sign);
        sendMessage.setText(text);
    }

    private void updateComment(SendMessage sendMessage, String product, String comment) {
        String text = familyWebService.updateProductComment(product, comment);
        sendMessage.setText(text);
    }

    private void emptyBasket(SendMessage sendMessage) {
        familyWebService.emptyBasket();
        sendMessage.setText("Все удалил))))");
    }
}
