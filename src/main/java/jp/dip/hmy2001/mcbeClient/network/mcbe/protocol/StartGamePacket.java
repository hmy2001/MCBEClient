package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class StartGamePacket extends GamePacket {

    public long entityUniqueId;
    public long entityRuntimeId;
    public int gamemode;
    public float playerX;
    public float playerY;
    public float playerZ;

    public byte getPacketId() {
        return ProtocolInfo.START_GAME_PACKET;
    }

    public void decodeBody(){
        entityUniqueId = readEntityUniqueId();
        entityRuntimeId = readEntityRuntimeId();
        gamemode = readUnsignedVarInt();//game mode
        playerX = readFloatLE();
        playerY = readFloatLE();
        playerZ = readFloatLE();
    }

}
