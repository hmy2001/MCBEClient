package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class BossEventPacket extends GamePacket {

    public long bossEntityUniqueId;
    public int eventType;

    public byte getPacketId() {
        return ProtocolInfo.BOSS_EVENT_PACKET;
    }

    public void decodeBody(){
        bossEntityUniqueId = readEntityUniqueId();
        eventType = readUnsignedVarInt();
        /*switch (eventType){
            case 0:

            break;
        }*/
    }

}
