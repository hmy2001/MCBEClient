package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class DisconnectPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.DISCONNECT_PACKET;

    public boolean hideDisconnectionScreen;
    public String message;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        hideDisconnectionScreen = readBoolean();
        if(!hideDisconnectionScreen){
            message = readString();
        }
    }

}
