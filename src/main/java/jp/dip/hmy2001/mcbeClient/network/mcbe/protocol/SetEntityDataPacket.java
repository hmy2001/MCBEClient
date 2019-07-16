package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class SetEntityDataPacket extends GamePacket {

    public int entityRuntimeId;

    public byte getPacketId() {
        return ProtocolInfo.SET_ENTITY_DATA_PACKET;
    }

    public void decodeBody(){
        entityRuntimeId = readVarLong();
        getEntityMetadata();
    }

}
