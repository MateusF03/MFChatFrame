package com.mateus.mfchatframe;

import com.mateus.mfchatframe.commands.FrameCommand;
import com.mateus.mfchatframe.core.FrameManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        FrameManager.getInstance().loadFrames(this);
        new FrameCommand(this);
    }
}
