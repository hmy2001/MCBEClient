package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class FullChunkDataPacket extends GamePacket {

    public int chunkX;
    public int chunkZ;
    public byte[] chunkData;

    public byte getPacketId() {
        return ProtocolInfo.FULL_CHUNK_DATA_PACKET;
    }

    public void decodeBody(){
        chunkX = readVarInt();
        chunkZ = readVarInt();
        chunkData = read(readUnsignedVarInt());
    }

}
