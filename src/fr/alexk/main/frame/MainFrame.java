package fr.alexk.main.frame;

import com.sun.xml.internal.ws.resources.DispatchMessages;
import fr.alexk.main.Main;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Scanner;

public class MainFrame extends JFrame implements ActionListener {


    public static String userString;

    private final JTextField user = new JTextField("User");
    private final JTextField password = new JTextField("Password");
    private final JButton button = new JButton("Connect");
    private final JButton chooseFile = new JButton("Choose File");
    private final JLabel isConnected = new JLabel();
    private final JLabel FileSelected = new JLabel();
    private final JButton sendFile = new JButton("Send File");

    private File file = new File("");
    public MainFrame(){

        JFrame frame = new JFrame("Share File");
        JPanel panel = new JPanel();
        frame.getContentPane();

        Dimension size = button.getPreferredSize();
        Dimension size1 = user.getPreferredSize();
        Dimension size2 = user.getPreferredSize();
        Dimension size5 = FileSelected.getPreferredSize();
        Dimension size3 = chooseFile.getPreferredSize();
        Dimension size4 = sendFile.getPreferredSize();
        user.setBounds(250 - 100 / 2, 30, 100, size1.height);
        password.setBounds(250 - 100 / 2, 50, 100, size2.height);
        button.setBounds(250 - size.width / 2, 80, size.width, size.height);
        isConnected.setBounds(200, 120, 200, 20);
        chooseFile.setBounds(10, 30, size3.width, size3.height);
        sendFile.setBounds(10, 60, size4.width, size4.height);
        FileSelected.setBounds(10, 90, 200, 20);
        panel.setLayout(null);
        button.addActionListener(this);
        chooseFile.addActionListener(this);
        sendFile.addActionListener(this);
        FileSelected.setText("Aucun fichier");
        isConnected.setText("Vous n'êtes pas connectée.");
        panel.add(button);
        panel.add(user);
        panel.add(FileSelected);
        panel.add(password);
        panel.add(isConnected);
        panel.add(chooseFile);
        panel.add(sendFile);
        sendFile.setEnabled(false);
        chooseFile.setEnabled(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setVisible(true);
        frame.setResizable(false);

    }

    int use = 0;
    public String getFile;
    @Override
    public void actionPerformed(ActionEvent e) {



        Object source = e.getSource();

        if(source == button && use == 0){
            isConnected.setText("Connection en cours...");
            user.setEnabled(false);
            password.setEnabled(false);
            userString = user.getText();
            button.setEnabled(false);
            try {
                main();
                if(passwordServer.equals(password.getText().substring(0, 4))){
                    isConnected.setText("Vous êtes connectée.");
                    sendFile.setEnabled(true);
                    chooseFile.setEnabled(true);
                } else {
                    user.setEnabled(true);
                    password.setEnabled(true);
                    button.setEnabled(true);
                    isConnected.setText("Mauvaise identifiant.");
                }
            } catch (UnknownHostException | SocketException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (source == chooseFile) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("C:\\Users"));
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                System.out.println(file.getName());
                FileSelected.setText(file.getName());
            }
        }
        if(source == sendFile){
            sendFileToFTP();
        }

    }

    // CONNECTION

    DatagramSocket datagramSocket;
    InetAddress inetAddress;
    private byte[] buffer = new byte[2048];
    private byte[] buffer1 = new byte[2048];

    public String passwordServer;

    public void sendThenReceive(){
        //Scanner scanner = new Scanner(System.in);
            try {
                String message = user.getText();
                buffer = message.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 1234);
                datagramSocket.send(datagramPacket);
                datagramSocket.receive(datagramPacket);
                String messageFromServer = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                passwordServer = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            } catch (IOException e){
                e.printStackTrace();
            }
    }

    public void main() throws UnknownHostException, SocketException {
        datagramSocket = new DatagramSocket();
        inetAddress = InetAddress.getByName("localhost");
        sendThenReceive();
    }

    private final String hostname = "localhost";
    private final int port = 21;

    public void sendFileToFTP(){
        Notification notification = new Notification();
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(hostname, port);
            ftp.login("User", "helloboy");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream inputStream = Files.newInputStream(file.toPath());
            String RemoteFile = file.getName();
            System.out.println("Start uploading file !");
            boolean done = ftp.storeFile(RemoteFile, inputStream);
            inputStream.close();
            if(done){
                notification.sendNotificationTypeInfo("The file is uploaded.");
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(ftp.isConnected()){
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
