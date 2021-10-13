package ru.makar.makareshfamilybot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.makar.makareshfamilybot.web.FamilyWebService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BotKeyboardsCreator {

    private final FamilyWebService familyWebService;

    public InlineKeyboardMarkup initSavedProductsKeyboard() {
        List<List<InlineKeyboardButton>> buttoms = new ArrayList<>();
        List<InlineKeyboardButton> collumn = new ArrayList<>();
        List<String> listResponseEntity = familyWebService.listExistedProducts();
        if (!listResponseEntity.isEmpty()) {
            for (String productName : listResponseEntity) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(productName);
                inlineKeyboardButton.setCallbackData(productName);
                collumn.add(inlineKeyboardButton);
                if (collumn.size() == 3) {
                    buttoms.add(collumn);
                    collumn = new ArrayList<>();
                }
            }
        }
        InlineKeyboardButton buttonOther = new InlineKeyboardButton();
        buttonOther.setText("Другой");
        buttonOther.setCallbackData("Другой");
        collumn.add(buttonOther);
        buttoms.add(collumn);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttoms);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initYesNoKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButtonYes = new InlineKeyboardButton();
        inlineKeyboardButtonYes.setText("Yes");
        inlineKeyboardButtonYes.setCallbackData("Yes");
        row.add(inlineKeyboardButtonYes);
        InlineKeyboardButton inlineKeyboardButtonNo = new InlineKeyboardButton();
        inlineKeyboardButtonNo.setText("No");
        inlineKeyboardButtonNo.setCallbackData("No");
        row.add(inlineKeyboardButtonNo);
        buttons.add(row);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initBasketProductsKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> collumn = new ArrayList<>();
        List<String> listResponseEntity = familyWebService.getProductsFromBasket();
        if (!listResponseEntity.isEmpty()) {
            for (String productName : listResponseEntity) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(productName);
                inlineKeyboardButton.setCallbackData(productName);
                collumn.add(inlineKeyboardButton);
                if (collumn.size() == 3) {
                    buttons.add(collumn);
                    collumn = new ArrayList<>();
                }
            }
            if (!collumn.isEmpty()) {
                buttons.add(collumn);
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(buttons);
            return inlineKeyboardMarkup;
        }
        return null;
    }

    public InlineKeyboardMarkup initQuantityKeyboard() {
        List<String> signs = List.of("+", "-");
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (String sign : signs) {
            InlineKeyboardButton inlineKeyboardButtonPlusOne = new InlineKeyboardButton();
            inlineKeyboardButtonPlusOne.setText(sign + "1");
            inlineKeyboardButtonPlusOne.setCallbackData(sign + "1");
            row.add(inlineKeyboardButtonPlusOne);
            InlineKeyboardButton inlineKeyboardButtonNo = new InlineKeyboardButton();
            inlineKeyboardButtonNo.setText(sign + "100");
            inlineKeyboardButtonNo.setCallbackData(sign + "100");
            row.add(inlineKeyboardButtonNo);
            buttons.add(row);
            row = new ArrayList<>();
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}
