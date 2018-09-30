package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class PlayStatusPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.PLAY_STATUS_PACKET;

    public int status;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        status = readInt();
    }

}
