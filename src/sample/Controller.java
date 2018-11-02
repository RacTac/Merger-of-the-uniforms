package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

public class Controller {

    public TextField mcName;
    public Button mergeButton;
    public Label insufficientName;
    public ImageView playerSkin;
    public Hyperlink hyperlink;
    private String uuid;


    public void onEnter(ActionEvent event) {
        if(event.getSource() == mcName || event.getSource() == mergeButton) {
            insufficientName.setTextFill(javafx.scene.paint.Color.web("#FF0000"));
            String mcNameText = mcName.getText();
            if(mcNameText.length() < 4 || mcNameText.length() > 16 || !mcNameText.matches("^[a-zA-Z0-9_]+$")) {
                insufficientName.setText("Name must be 4-16 characters long!");
                playerSkin.setImage(null);
                return;
            }
            mcName.clear();
            try {
                uuid = UUIDGetter.getUUID(mcNameText);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
            if(uuid == null) {
                insufficientName.setText("Player doesn't exist!");
                playerSkin.setImage(null);
                return;
            }
            try {
                BufferedImage playerSkinPNG = ImageIO.read(new URL("https://crafatar.com/skins/" + uuid.replace("-", "") + ".png"));
                BufferedImage uniformOverlay = ImageIO.read(new URL("https://i.imgur.com/hEaW7Tq.png"));
                int w = Math.max(playerSkinPNG.getWidth(), uniformOverlay.getWidth());
                int h = Math.max(playerSkinPNG.getHeight(), uniformOverlay.getHeight());
                BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = playerSkinPNG.getGraphics();
                Graphics2D g2D = (Graphics2D) graphics;
                Color color = new Color(0,0,0,0);
                g2D.setColor(color);
                g2D.drawRect(0,16,64,48);
                g2D.fillRect(0,16,64,48);
                Graphics2D combinedGraphics = (Graphics2D) combined.getGraphics();
                combinedGraphics.drawImage(playerSkinPNG, 0,0,null);
                combinedGraphics.drawImage(uniformOverlay,0,0,null);
                playerSkin.setImage(SwingFXUtils.toFXImage(combined, null));
                File file = new File(mcNameText + "_uniform.png");
                ImageIO.write(combined, "png", file);
                hyperlink.setVisible(true);
                hyperlink.setOnAction(e -> {
                    if(Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (Exception ex) {
                            System.out.println(e);
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            insufficientName.setTextFill(javafx.scene.paint.Color.web("#00ff00"));
            insufficientName.setText("Operation successful!");
        }
    }

}