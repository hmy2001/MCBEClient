package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class BatchPacket extends GamePacket {

    public byte[] payload;

    public byte getPacketId() {
        return ProtocolInfo.BATCH_PACKET;
    }

    public void encode(){
        writeByte(getPacketId());
        encodeBody();
    }

    public void encodeBody(){
        write(payload);
    }

    public void decode() {
        readByte();
        decodeBody();
    }

    public void decodeBody(){
        payload = read(remaining());
    }

}
