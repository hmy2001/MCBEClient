package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ClientToServerHandshakePacket extends GamePacket {

    public byte getPacketId() {
        return ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET;
    }

    public void encodeBody(){
        //no payload
    }

}
