package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class SpawnParticleEffectPacket extends GamePacket {

    public int dimensionId;
    public int entityUniqueId;
    public float x;
    public float y;
    public float z;
    public String particleName;

    public byte getPacketId() {
        return ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET;
    }

    public void decodeBody(){
        dimensionId = readByte();
        entityUniqueId = readVarLong();
        x = readFloatLE();
        y = readFloatLE();
        z = readFloatLE();
        particleName = readString();
    }

}
