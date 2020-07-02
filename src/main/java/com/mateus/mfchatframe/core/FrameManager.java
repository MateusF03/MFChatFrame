package com.mateus.mfchatframe.core;

import org.bukkit.plugin.Plugin;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FrameManager {

    private static FrameManager instance;
    private FrameManager() {}
    private List<Frame> frames = new ArrayList<>();

    public static FrameManager getInstance() {
        if (instance == null) {
            synchronized (FrameManager.class) {
                if (instance == null) instance = new FrameManager();
            }
        }
        return instance;
    }

    public void loadFrames(Plugin plugin) {
        File framesFolder = new File(plugin.getDataFolder() + "/frames");
        if (!framesFolder.exists()) framesFolder.mkdirs();
        File[] files = framesFolder.listFiles();
        assert files != null;
        long runtime = System.currentTimeMillis();
        int num = 0;
        for (File file : files) {
            try {
                if (isGif(file)) {
                    frames.add(new GifFrame(file,plugin));
                } else if (isImage(file)) {
                    frames.add(new ImageFrame(file));
                }
                num++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        plugin.getLogger().info("Loaded " + num + " frames in " + (System.currentTimeMillis() - runtime) + "ms");
    }

    private boolean isImage(File file) {
        try {
            ImageIO.read(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isGif(File file) throws IOException {
        ImageInputStream stream = ImageIO.createImageInputStream(file);

        Iterator<ImageReader> iterator = ImageIO.getImageReaders(stream);
        if (!iterator.hasNext()) {
            return false;
        }
        ImageReader reader = iterator.next();
        reader.setInput(stream, true, true);
        String formatName = reader.getFormatName();
        reader.dispose();
        stream.close();
        return formatName.equals("gif");
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public Frame getFrameByName(String name, boolean ignoreCase) {
        if (ignoreCase) {
            return frames.stream().filter(frame -> frame.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        } else {
            return frames.stream().filter(frame -> frame.name().equals(name)).findFirst().orElse(null);
        }
    }
}
