package jp.dip.hmy2001.mcbeClient.utils;

import com.whirvis.jraknet.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BinaryStream extends Packet {

    public String readString(){
        return new String(read(readUnsignedVarInt()));
    }

    public float readFloatLE() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putFloat(readFloat());
        byteBuffer.flip();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.getFloat();
    }

    public void writeFloatLE(float value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putFloat(value);
        byteBuffer.flip();

        write(byteBuffer.array());
    }

    public void writeVarInt(int v){
        v = (v << 32 >> 32);
        writeUnsignedVarInt((v << 1) ^ (v >> 31));
    }

    public void writeUnsignedVarInt(int value){
        byte[] buf = new byte[5];
        value &= 0xffffffff;

        for(int i = 0; i < 5; ++i){
            if((value >> 7) != 0){
                buf[i] = (byte) (value | 0x80);
            }else{
                buf[i] = (byte) (value & 0x7f);
                write(Arrays.copyOf(buf, i + 1));
                return;
            }
            value = ((value >> 7) & (Integer.MAX_VALUE >> 6));
        }
    }

    public int readVarInt(){
        int raw = readUnsignedVarInt();
        int temp = (((raw << 63) >> 63) ^ raw) >> 1;
        return temp ^ (raw & (1 << 63));
    }

    public int readUnsignedVarInt() {
        int value = 0;

        for(int i = 0; i <= 63; i += 7){
            byte b = readByte();
            value |= ((b & 0x7f) << i);
            if((b & 0x80) == 0){
                return value;
            }
        }
        return value;
    }

    public void writeVarLong(int v){
        writeUnsignedVarLong((v << 1) ^ (v >> 63));
    }

    public void writeUnsignedVarLong(int value){
        byte[] buf = new byte[10];

        for(int i = 0; i < 10; ++i){
            if((value >> 7) != 0){
                buf[i] = (byte) (value | 0x80);
            }else{
                buf[i] = (byte) (value & 0x7f);
                write(Arrays.copyOf(buf, i + 1));
                return;
            }
            value = ((value >> 7) & (Integer.MAX_VALUE >> 6));
        }
    }

    public int readVarLong(){
        int raw = readUnsignedVarLong();
        int temp = (((raw << 63) >> 63) ^ raw) >> 1;
        return temp ^ (raw & (1 << 63));
    }

    public int readUnsignedVarLong(){
        int value = 0;
        for(int i = 0; i <= 63; i += 7){
            byte b = readByte();
            value |= ((b & 0x7f) << i);
            if((b & 0x80) == 0){
                return Integer.parseUnsignedInt(Integer.toUnsignedString(value));
            }
        }

        return value;
    }

}
