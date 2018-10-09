package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class InventoryContentPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.INVENTORY_CONTENT_PACKET;

    public int windowId;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        windowId = readUnsignedVarInt();
        int count = readUnsignedVarInt();

        for(int i = 0; i < count; i++){
            readSlot();
        }
    }

}
