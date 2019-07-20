package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class AddPlayerPacket extends GamePacket {

    public String username;
    public long entityUniqueId;
    public long entityRuntimeId;

    public byte getPacketId() {
        return ProtocolInfo.ADD_ENTITY_PACKET;
    }

    public void decodeBody(){
        read(16);//UUID
        username = readString();
        entityUniqueId = readEntityUniqueId();
        entityRuntimeId = readEntityRuntimeId();
    }

}
