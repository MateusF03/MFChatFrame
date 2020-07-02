package com.mateus.mfchatframe.core;

import com.mateus.mfchatframe.utils.ChainedTextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GifFrame implements Frame {

    private final List<BufferedImage> images = new ArrayList<>();
    private final Plugin plugin;
    private final String name;

    public GifFrame(File file, Plugin plugin) {
        name = file.getName();
        this.plugin = plugin;
        try {
            String[] imageatt = new String[]{
                    "imageLeftPosition",
                    "imageTopPosition",
                    "imageWidth",
                    "imageHeight"
            };

            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream ciis = ImageIO.createImageInputStream(file);
            reader.setInput(ciis, false);

            int noi = reader.getNumImages(true);
            BufferedImage master = null;

            for (int i = 0; i < noi; i++) {
                BufferedImage image = reader.read(i);
                IIOMetadata metadata = reader.getImageMetadata(i);

                Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
                NodeList children = tree.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node nodeItem = children.item(j);

                    if(nodeItem.getNodeName().equals("ImageDescriptor")){
                        Map<String, Integer> imageAttr = new HashMap<>();

                        for (String s : imageatt) {
                            NamedNodeMap attr = nodeItem.getAttributes();
                            Node attnode = attr.getNamedItem(s);
                            imageAttr.put(s, Integer.valueOf(attnode.getNodeValue()));
                        }
                        if(i==0){
                            master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
                        }
                        master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
                    }
                }
                assert master != null;
                BufferedImage frame = copyImage(master);
                images.add(frame);
            }
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
        new BukkitRunnable() {
            int i = 0;
            final int num = images.size() - 1;
            @Override
            public void run() {
                i++;
                if (i > num) {
                    cancel();
                } else {
                    ChainedTextComponent ctc = new ChainedTextComponent("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    BufferedImage image = resize(images.get(i),24,21);
                    try {
                        ImageIO.write(image, "png", new File(plugin.getDataFolder(), i+".png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (int y = 0; y < image.getHeight(); y++) {
                        for (int x = 0; x < image.getWidth(); x++) {
                            Color color = new Color(image.getRGB(x,y));
                            ctc.add(new ChainedTextComponent("█").setColor(color));
                        }
                        ctc.add(new ChainedTextComponent("\n"));
                    }
                    player.spigot().sendMessage(ctc.build());
                }
            }
        }.runTaskTimer(plugin,0L, 5L);
    }
    private BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image,0,0,width,height,null);
        g.dispose();
        return resizedImage;
    }
    private BufferedImage copyImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = createCompatibleImage(img);
        Graphics graphics = newImage.createGraphics();

        int x = (width - img.getWidth()) / 2;
        int y = (height - img.getHeight()) / 2;

        graphics.drawImage(img, x, y, img.getWidth(), img.getHeight(), null);
        graphics.dispose();

        return newImage;
    }
    private BufferedImage createCompatibleImage(BufferedImage image) {
        return getGraphicsConfiguration().createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
    }

    private GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }
}
