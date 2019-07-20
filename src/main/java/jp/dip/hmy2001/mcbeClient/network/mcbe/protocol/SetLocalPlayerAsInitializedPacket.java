package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class SetLocalPlayerAsInitializedPacket extends GamePacket {

    public long entityRuntimeId;

    public byte getPacketId() {
        return ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET;
    }

    public void encodeBody(){
        writeUnsignedVarLong(entityRuntimeId);
    }

}
