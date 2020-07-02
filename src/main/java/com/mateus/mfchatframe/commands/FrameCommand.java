package com.mateus.mfchatframe.commands;

import com.mateus.mfchatframe.core.Frame;
import com.mateus.mfchatframe.core.FrameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class FrameCommand implements CommandExecutor {

    public FrameCommand(JavaPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("frame")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Incorrect usage of the command, use: " + ChatColor.DARK_RED + "/frame [list/frame name]");
            } else {
                if (args[0].equalsIgnoreCase("list")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(ChatColor.LIGHT_PURPLE).append("List:\n").append(ChatColor.GREEN);
                    for (Frame frame : FrameManager.getInstance().getFrames()) {
                        stringBuilder.append(frame.name()).append(", ");
                    }
                    player.sendMessage(stringBuilder.toString().substring(0, stringBuilder.length() - ", ".length()));
                } else {
                    Frame frame = FrameManager.getInstance().getFrameByName(args[0], true);
                    if (frame == null) {
                        player.sendMessage(ChatColor.RED + "This frame does not exist");
                    } else {
                        frame.display(player);
                    }
                }
            }
        }
        return false;
    }
}
