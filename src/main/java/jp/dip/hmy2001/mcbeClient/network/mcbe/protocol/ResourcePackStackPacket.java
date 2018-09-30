package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ResourcePackStackPacket extends GamePacket {
    byte NETWORK_ID = ProtocolInfo.RESOURCE_PACK_STACK_PACKET;

    public int status;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        writeByte(status);
        writeShortLE(0);//TODO
    }

}
