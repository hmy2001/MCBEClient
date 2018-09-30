package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class SetLocalPlayerAsInitializedPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET;

    public int entityRuntimeId;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encodeBody(){
        writeUnsignedVarLong(entityRuntimeId);
    }

}
