package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ClientToServerHandshakePacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encodeBody(){
        //no payload
    }

}
