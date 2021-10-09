package ru.makar.makareshfamilybot.bot.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.makar.makareshfamilybot.web.FamilyWebService;

@Service
@RequiredArgsConstructor
public class CallbackService {

    private final FamilyWebService familyWebService;

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
                    addProduct(sendMessage, selectedData);
                }
                break;
            case "SELECT":
                if ("Yes".equals(selectedData)) {
                    addProduct(sendMessage, split[2]);
                } else {
                    sendMessage.setText("OK. Не добавлю...");
                }
            case "UPDATE COMMENT":
                sendMessage.setText("Я хз как эту фичу реализовать так что пусть тут будут текст гыгыгыг))))");
            case "UPDATE QUANTITY":

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

    private void updateQuantity() {
        //TODO На колбек UPDATE QUANTITY появляются кнопки +1 +100, на них добавить тоже колбеки с дополнительной кнопкой возврата к списку продуктов к корзине
    }

    private void updateComment() {
        //TODO Единственное что придумал так это то, что перед комментарием писать "Комментарий:", тогда в методе onSimpleMessage можно будет понять что это комментарий, а не новый продукт
    }
}
