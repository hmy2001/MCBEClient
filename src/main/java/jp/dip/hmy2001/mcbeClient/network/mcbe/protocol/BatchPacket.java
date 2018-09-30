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
            Deflater compresser = new Deflater(Deflater.DEFLATED);
            compresser.setInput(payload);
            compresser.finish();
            int length = compresser.deflate(output);
            compresser.end();

            write(Arrays.copyOf(output, length));
        }
    }

    public void decode() {
        readByte();
        decodeBody();
    }

    public void decodeBody(){
        byte[] payload = new byte[1024 * 1024 * 64];

        Inflater decompresser = new Inflater();
        decompresser.setInput(read(remaining()));

        try{
            int length = decompresser.inflate(payload);
            decompresser.end();

            this.payload = Arrays.copyOf(payload, length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
