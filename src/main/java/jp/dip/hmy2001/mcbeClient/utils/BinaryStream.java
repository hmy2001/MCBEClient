package jp.dip.hmy2001.mcbeClient.utils;

import com.whirvis.jraknet.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BinaryStream extends Packet {

    public String readString(){
        return new String(read(readUnsignedVarInt()));
    }

    protected float readFloatLE() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putFloat(readFloat());
        byteBuffer.flip();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.getFloat();
    }

    protected void writeFloatLE(float value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putFloat(value);
        byteBuffer.flip();

        write(byteBuffer.array());
    }

    protected void writeVarInt(int v){
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

    protected int readVarInt(){
        int raw = readUnsignedVarInt();
        int temp = (((raw << 63) >> 63) ^ raw) >> 1;
        return temp ^ (raw & (1 << 63));
    }

    public int readUnsignedVarInt() {
        int value = 0;

        for(int i = 0; i <= 28; i += 7){
            byte b = readByte();
            value |= ((b & 0x7f) << i);
            if((b & 0x80) == 0){
                return value;//TODO: check it
            }
        }
        return value;
    }

    protected void writeVarLong(int v){
        writeUnsignedVarLong((v << 1) ^ (v >> 63));
    }

    protected void writeUnsignedVarLong(int value){
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

    protected int readVarLong(){
        int raw = readUnsignedVarLong();
        int temp = (((raw << 63) >> 63) ^ raw) >> 1;
        return temp ^ (raw & (1 << 63));
    }

    protected int readUnsignedVarLong(){
        int value = 0;
        for(int i = 0; i <= 63; i += 7){
            byte b = readByte();
            value |= ((b & 0x7f) << i);
            if((b & 0x80) == 0){
                return value;//TODO: check it
            }
        }

        return value;
    }

}
