package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class LevelEventPacket extends GamePacket {

    public int eventId;

    public byte getPacketId() {
        return ProtocolInfo.LEVEL_EVENT_PACKET;
    }

    public void decodeBody(){
        eventId = readUnsignedVarInt();
    }

}
