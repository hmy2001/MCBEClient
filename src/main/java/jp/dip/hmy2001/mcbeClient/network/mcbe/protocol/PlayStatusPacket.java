package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class PlayStatusPacket extends GamePacket {

    public int status;

    public byte getPacketId() {
        return ProtocolInfo.PLAY_STATUS_PACKET;
    }

    public void decodeBody(){
        status = readInt();
    }

}
