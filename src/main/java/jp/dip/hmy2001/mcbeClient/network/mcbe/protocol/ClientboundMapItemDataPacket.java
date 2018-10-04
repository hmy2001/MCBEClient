package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ClientboundMapItemDataPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET;

    public int mapId;
    public int type;
    public int dimensionId;
    public int scale;
    public int[][] colors;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        mapId = readVarLong();
        type = readUnsignedVarInt();
        dimensionId = readByte();

        if((type & 0x08) != 0){//eid
            int eidCount = readUnsignedVarInt();
            for (int i = 0; i < eidCount; eidCount++){
                readVarLong();
            }
        }

        scale = readByte();

        if((type & 0x04) != 0){//Decorations
            int trackedEntityCount = readUnsignedVarInt();
            for(int i = 0; i < trackedEntityCount; i++){
                int objectType = readIntLE();
                if(objectType == 0){
                    int objectEntityId = readVarInt();
                }else{
                    int x = readVarInt();
                    int y = readUnsignedVarInt();
                    int z = readVarInt();
                }
            }

            int decorationCount = readUnsignedVarInt();
            for(int i = 0; i < decorationCount; i++){
                int decorationType = readByte();
                int decorationRotation = readByte();
                int xOffset = readByte();
                int yOffset = readByte();
                String decorationName = readString();
                int color = readUnsignedVarInt();
            }
        }

        if((type & 0x02) != 0){//Textures
            int width = readVarInt();
            int height = readVarInt();
            int xOffset = readVarInt();
            int yOffset = readVarInt();
            int count = readUnsignedVarInt();

            colors = new int[height][width];
            for(int y = 0; y < height; ++y){
                for(int x = 0; x < width; ++x){
                    colors[y][x] = readUnsignedVarInt();
                }
            }
        }
    }

}
