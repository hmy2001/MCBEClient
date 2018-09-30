package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class MobEquipmentPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.MOB_EQUIPMENT_PACKET;

    public int entityRuntimeId;
    public int inventorySlot;
    public int hotbarSlot;
    public int windowId;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encodeBody(){

    }

    public void decodeBody(){
        entityRuntimeId = readUnsignedVarLong();
        readSlot();
        inventorySlot = readByte();
        hotbarSlot = readByte();
        windowId = readByte();
    }

}
