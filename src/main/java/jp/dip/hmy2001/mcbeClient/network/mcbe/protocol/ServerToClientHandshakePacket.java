package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ServerToClientHandshakePacket extends GamePacket {

    public String jwt;

    public byte getPacketId() {
        return ProtocolInfo.SERVER_TO_CLIENT_HANDSHAKE_PACKET;
    }

    public void decodeBody(){
        jwt = readString();
    }

}
