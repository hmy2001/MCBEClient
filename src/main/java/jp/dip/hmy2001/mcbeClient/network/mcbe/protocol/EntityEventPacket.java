package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class EntityEventPacket extends GamePacket {

    public int eventId;

    public byte getPacketId() {
        return ProtocolInfo.ENTITY_EVENT_PACKET;
    }

    public void decodeBody(){
        readVarLong();
        eventId = readUnsignedVarInt();
    }

}
