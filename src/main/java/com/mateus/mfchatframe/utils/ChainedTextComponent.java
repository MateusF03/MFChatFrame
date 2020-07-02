package com.mateus.mfchatframe.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;

public class ChainedTextComponent {

    private TextComponent textComponent;

    public ChainedTextComponent(String text) {
        textComponent = new TextComponent(text);
    }

    public ChainedTextComponent setColor(ChatColor color) {
        textComponent.setColor(color);
        return this;
    }

    public ChainedTextComponent setColor(Color color) {
        textComponent.setColor(ChatColor.of(color));
        return this;
    }

    public ChainedTextComponent add(ChainedTextComponent ctc) {
        textComponent.addExtra(ctc.build());
        return this;
    }

    public TextComponent build() {
        return textComponent;
    }
}
