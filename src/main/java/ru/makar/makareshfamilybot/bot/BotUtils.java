package ru.makar.makareshfamilybot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.makar.makareshfamilybot.model.BasketProduct;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotUtils {

//    private final AbsSender absSender;
    private final List<Long> usersList;
    private final Set<Map<Long, String>> potentialUsers;

    public boolean checkUser(Long id) {
        return usersList.contains(id);
    }

    protected Set<Map<Long, String>> getPotentialUsers() {
        return potentialUsers;
    }

    public void addPotenialUser(Long id, String name) {
        potentialUsers.add(Map.of(id, name));
    }

    public String formatedMessege(List<BasketProduct> basketProducts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (BasketProduct basketProduct : basketProducts) {
            stringBuilder.append(
                    String.format(
                            "%s | %s | %s",
                            basketProduct.getProductName(),
                            basketProduct.getQuantity(),
                            basketProduct.getComment()
                    )
            );
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public SendMessage unauthMessegeExecution(SendMessage sendMessage) {
        sendMessage.setText("А вы тут не авторизованы судддрь");
        return sendMessage;
    }
}
