package jp.dip.hmy2001.mcbeClient.network.mcbe;

import jp.dip.hmy2001.mcbeClient.utils.BinaryStream;

public class GamePacket extends BinaryStream {
    final byte NETWORK_ID = (byte) 0xff;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encode(){
        writeUnsignedVarInt(getPacketId());
        encodeBody();
    }

    public void encodeBody(){

    }

    public void decode(){
        readUnsignedVarInt();
        decodeBody();
    }

    public void decodeBody(){

    }

    public void readSlot(){
        int id = readVarInt();

        if(id == 0){
            return;
        }

        int auxValue = readVarInt();
        int data = auxValue >> 8;
        if(data == 0x7fff){
            data = -1;
        }
        int cnt = auxValue & 0xff;

        int nbtLen = readShortLE();
        byte[] nbt;

        if(nbtLen > 0){
            nbt = read(nbtLen);
        }

        //TODO
        for(int i = 0, canPlaceOn = readVarInt(); i < canPlaceOn; ++i){
            readString();
        }

        //TODO
        for(int i = 0, canDestroy = readVarInt(); i < canDestroy; ++i){
            readString();
        }
    }

}
