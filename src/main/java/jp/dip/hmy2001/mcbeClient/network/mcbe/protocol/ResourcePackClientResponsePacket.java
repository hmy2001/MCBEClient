package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ResourcePackClientResponsePacket extends GamePacket {
    byte NETWORK_ID = ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;

    public int status;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encodeBody(){
        writeByte(status);
        writeShortLE(0);//TODO
    }

}
