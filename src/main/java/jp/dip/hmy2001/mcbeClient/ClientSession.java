package jp.dip.hmy2001.mcbeClient;

import com.whirvis.jraknet.client.RakNetClient;
import jp.dip.hmy2001.mcbeClient.utils.CommandReader;

import java.net.*;

public class ClientSession extends Thread{
    private InetAddress serverAddress;
    private int serverPort;
    private RakNetClient rakNetClient;

    public ClientSession(MCBEClient client, InetAddress serverAddress, int serverPort, String username, String clientUUID){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

        rakNetClient = new RakNetClient();
        rakNetClient.setMaximumTransferUnits(1400);
        rakNetClient.addListener(new ClientListener(client, this, username, clientUUID));
    }

    public String getServerAddress() {
        return serverAddress.getHostAddress();
    }

    public int getServerPort() {
        return serverPort;
    }

    public void run(){
        try {
            rakNetClient.connect(serverAddress, serverPort);
        }catch (Exception e){
            CommandReader.getInstance().stashLine();
            e.printStackTrace();
            CommandReader.getInstance().unstashLine();
        }
    }

    public void shutdown(){
        if(rakNetClient.isConnected()){
            rakNetClient.disconnect();
        }

        rakNetClient.shutdown();
    }

}
