package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class RequestChunkRadiusPacket extends GamePacket {

    public int radius;

    public byte getPacketId() {
        return ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;
    }

    public void encodeBody(){
        writeVarInt(radius);
    }

}
