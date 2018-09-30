package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ServerToClientHandshakePacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.SERVER_TO_CLIENT_HANDSHAKE_PACKET;

    public String jwt;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        jwt = readString();
    }

}
