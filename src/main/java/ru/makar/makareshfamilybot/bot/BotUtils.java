package ru.makar.makareshfamilybot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
}
