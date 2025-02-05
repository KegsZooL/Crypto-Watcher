package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.menu.service.MenuHistoryManager;
import com.github.kegszool.bot.menu.service.MenuRegistry;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Log4j2
public class MenuSwitchCommand extends CallbackCommand {

    private final MenuRegistry menuRegistry;
    private final MenuHistoryManager menuHistoryManager;
    private final MessageUtils messageUtils;

    @Autowired
    public MenuSwitchCommand(
            MenuRegistry menuRegistry,
            MenuHistoryManager menuHistoryManager,
            MessageUtils messageUtils
    ) {
        this.menuRegistry = menuRegistry;
        this.menuHistoryManager = menuHistoryManager;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return menuRegistry.isContained(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        String menuName = switchAndGetMenu(query);
        var message = messageUtils.createEditMessageByMenuName(query, menuName);
        log.info("Switched menu successfully. Menu name: {}", menuName);
        return message;
    }

    private String switchAndGetMenu(CallbackQuery query) {
        String chatId = messageUtils.extractChatId(query);
        String currentMenuName = query.getData();
        menuHistoryManager.recordMenu(chatId, currentMenuName);
        return currentMenuName;
    }
}