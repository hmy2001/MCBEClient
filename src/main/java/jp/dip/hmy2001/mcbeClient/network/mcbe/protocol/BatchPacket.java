package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

import java.util.Arrays;
import java.util.zip.*;

public class BatchPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.BATCH_PACKET;

    public byte[] payload;
    public boolean isEncryption = false;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encode(){
        writeByte(getPacketId());
        encodeBody();
    }

    public void encodeBody(){
        if(isEncryption){
            write(payload);
        }else{
            byte[] output = new byte[100000];
            Deflater compressor = new Deflater(Deflater.DEFLATED);
            compressor.setInput(payload);
            compressor.finish();
            int length = compressor.deflate(output);
            compressor.end();

            write(Arrays.copyOf(output, length));
        }
    }

    public void decode() {
        readByte();
        decodeBody();
    }

    public void decodeBody(){
        byte[] payload = new byte[1024 * 1024 * 64];

        Inflater decompressor = new Inflater();
        decompressor.setInput(read(remaining()));

        try{
            int length = decompressor.inflate(payload);
            decompressor.end();

            this.payload = Arrays.copyOf(payload, length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
