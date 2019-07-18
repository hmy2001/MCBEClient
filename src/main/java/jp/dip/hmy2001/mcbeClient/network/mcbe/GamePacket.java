package jp.dip.hmy2001.mcbeClient.network.mcbe;

import jp.dip.hmy2001.mcbeClient.utils.BinaryStream;

public class GamePacket extends BinaryStream {

    public byte getPacketId() {
        return (byte) 0xff;
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
        if(nbtLen == 0xffff){
            int c = readByte();
            if(c == 1){
                System.out.println("read NBT");
            }
        }

        for(int i = 0, canPlaceOn = readVarInt(); i < canPlaceOn; ++i){
            read(readUnsignedVarInt());
        }
        //TODO
        for(int i = 0, canDestroy = readVarInt(); i < canDestroy; ++i){
            read(readUnsignedVarInt());
        }

        if(id == 513){
            readVarLong(); //"blocking tick" (ffs mojang)
        }
    }

    public void getEntityMetadata(){
        int count = readUnsignedVarInt();
        for(int i = 0; i < count; ++i){
            int key = readUnsignedVarInt();
            int type = readUnsignedVarInt();

            switch(type){
                case 0:
                    readByte();
                    break;
                case 1:
                    readShortLE();
                    break;
                case 2:
                    readVarInt();
                    break;
                case 3:
                    readFloatLE();
                    break;
                case 4:
                    read(readUnsignedVarInt());
                    break;
                case 5:
                    readSlot();
                    break;
                case 6:
                    readVarInt();
                    readUnsignedVarInt();
                    readVarInt();
                    break;
                case 7:
                    readVarLong();
                    break;
                case 8:
                    readFloatLE();
                    readFloatLE();
                    readFloatLE();
                    break;
            }
        }
    }

    public int readEntityUniqueId(){
        int temp = readVarLong();
        return temp;
    }

    public int readEntityRuntimeId(){
        int temp = readUnsignedVarLong();
        return temp;
    }

}
