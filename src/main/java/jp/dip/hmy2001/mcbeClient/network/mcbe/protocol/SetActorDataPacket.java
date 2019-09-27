package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class SetActorDataPacket extends GamePacket {

    public long entityRuntimeId;

    public byte getPacketId() {
        return ProtocolInfo.SET_ACTOR_DATA_PACKET;
    }

    public void decodeBody(){
        entityRuntimeId = readEntityRuntimeId();
        getEntityMetadata();
    }

}
