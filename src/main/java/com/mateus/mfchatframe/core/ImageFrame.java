package com.mateus.mfchatframe.core;

import com.mateus.mfchatframe.utils.ChainedTextComponent;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFrame implements Frame {

    private BufferedImage image;
    private final String name;

    public ImageFrame(File file) {
        name = file.getName();
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void display(Player player) {
        assert image != null;
        ChainedTextComponent ctc = new ChainedTextComponent("");
        BufferedImage newImage = resize(image, Math.min(image.getWidth(), 24), Math.min(image.getHeight(), 21));
        for (int y = 0; y < newImage.getHeight(); y++) {
            for (int x = 0; x < newImage.getWidth(); x++) {
                 Color color = new Color(newImage.getRGB(x,y));
                 ctc.add(new ChainedTextComponent("█").setColor(color));
            }
            ctc.add(new ChainedTextComponent("\n"));
        }
        player.spigot().sendMessage(ctc.build());
    }

    private BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image,0,0,width,height,null);
        g.dispose();
        return resizedImage;
    }
}
