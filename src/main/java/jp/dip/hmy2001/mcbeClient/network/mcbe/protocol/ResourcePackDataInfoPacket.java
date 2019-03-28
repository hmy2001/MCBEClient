package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ResourcePackDataInfoPacket extends GamePacket {

    public int status;

    public byte getPacketId() {
        return ProtocolInfo.RESOURCE_PACKS_INFO_PACKET;
    }

    public void decodeBody(){
        writeByte(status);
        writeShortLE(0);//TODO
    }

}
