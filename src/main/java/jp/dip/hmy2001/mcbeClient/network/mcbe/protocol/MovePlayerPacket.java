package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class MovePlayerPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.MOVE_PLAYER_PACKET;

    public int entityRuntimeId;
    public float playerX;
    public float playerY;
    public float playerZ;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encodeBody(){
        writeVarLong(entityRuntimeId);
        writeFloatLE(playerX);
        writeFloatLE(playerY);
        writeFloatLE(playerZ);
        writeFloatLE(0);
        writeFloatLE(0);
        writeFloatLE(0);
        writeByte(0);
        writeBoolean(true);
        writeVarLong(0);
        //TODO
    }

    public void decodeBody(){

    }

}
