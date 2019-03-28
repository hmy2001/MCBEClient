package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class DisconnectPacket extends GamePacket {

    public boolean hideDisconnectionScreen;
    public String message;

    public byte getPacketId() {
        return ProtocolInfo.DISCONNECT_PACKET;
    }

    public void decodeBody(){
        hideDisconnectionScreen = readBoolean();
        if(!hideDisconnectionScreen){
            message = readString();
        }
    }

}
