package fr.alexk.main.frame;

import java.awt.*;

public class Notification {

    public void sendNotificationTypeInfo(String text){
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("ico.png");
            TrayIcon trayIcon = new TrayIcon(image, "ShareFile");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("ShareFile");
            tray.add(trayIcon);
            trayIcon.displayMessage("ShareFile", text, TrayIcon.MessageType.INFO);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
