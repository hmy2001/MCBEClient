package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class RequestChunkRadiusPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;

    public int radius;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encodeBody(){
        writeVarInt(radius);
    }

}
