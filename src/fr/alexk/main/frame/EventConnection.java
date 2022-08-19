package fr.alexk.main.frame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class EventConnection {

    DatagramPacket datagramSocket;
    InetAddress inetAddress;
    private byte[] buffer = new byte[1024];

    public void onConnection(){
        //Scanner scanner = new Scanner(System.in);
        String message = "";
        buffer = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 1234);
        //datagramSocket.send(datagramPacket);
        //datagramSocket.receive(datagramPacket);
        String messageFromServer = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
    }

}
