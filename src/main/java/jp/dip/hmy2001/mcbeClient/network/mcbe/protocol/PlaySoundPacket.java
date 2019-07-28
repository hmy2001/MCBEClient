package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class PlaySoundPacket extends GamePacket {

    public String soundName;
    public int x;
    public int y;
    public int z;
    public float volume;
    public float pitch;

    public byte getPacketId() {
        return ProtocolInfo.PLAY_SOUND_PACKET;
    }

    public void decodeBody(){
        soundName = readString();
        x = readVarInt() / 8;
        y = readUnsignedVarInt() / 8;
        z = readVarInt() / 8;
        volume = readFloatLE();
        pitch = readFloatLE();
    }

}
