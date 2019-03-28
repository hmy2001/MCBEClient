package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ResourcePackStackPacket extends GamePacket {

    public int status;

    public byte getPacketId() {
        return ProtocolInfo.RESOURCE_PACK_STACK_PACKET;
    }

    public void decodeBody(){
        writeByte(status);
        writeShortLE(0);//TODO
    }

}
