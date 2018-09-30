package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class StartGamePacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.START_GAME_PACKET;

    public int entityUniqueId;
    public int entityRuntimeId;
    public int gamemode;
    public float playerX;
    public float playerY;
    public float playerZ;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        entityUniqueId = readVarLong();
        entityRuntimeId = readUnsignedVarLong();
        gamemode = readUnsignedVarInt();//game mode
        playerX = readFloatLE();
        playerY = readFloatLE();
        playerZ = readFloatLE();
    }

}
