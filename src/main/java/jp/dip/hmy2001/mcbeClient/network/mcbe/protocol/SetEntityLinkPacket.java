package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class SetEntityLinkPacket extends GamePacket {

    public long fromEntityUniqueId;
    public long toEntityUniqueId;
    public int type;

    public byte getPacketId() {
        return ProtocolInfo.SET_ENTITY_LINK_PACKET;
    }

    public void decodeBody(){
        fromEntityUniqueId = readEntityUniqueId();
        toEntityUniqueId = readEntityUniqueId();
        type = readByte();
    }

}
