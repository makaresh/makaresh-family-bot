package ru.makar.makareshfamilybot.bot.executor;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandExecutor {

    SendMessage run(Message message);

    String getCommand();
}
