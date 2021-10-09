package ru.makar.makareshfamilybot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.makar.makareshfamilybot.model.BasketProduct;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BotUtils {
    private final List<Long> usersList;
    private final Set<Map<Long, String>> potentialUsers;

    protected boolean checkUser(Long id) {
        return usersList.contains(id);
    }

    protected Set<Map<Long, String>> getPotentialUsers() {
        return potentialUsers;
    }

    protected void addPotenialUser(Long id, String name) {
        potentialUsers.add(Map.of(id, name));
    }

    protected String formatedMessege(List<BasketProduct> basketProducts) {
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
}
