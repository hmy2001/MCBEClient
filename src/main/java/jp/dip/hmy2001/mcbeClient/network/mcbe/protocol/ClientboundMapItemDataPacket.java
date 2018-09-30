package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ClientboundMapItemDataPacket extends GamePacket {
    final byte NETWORK_ID = ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET;

    public int mapId;
    public int type;
    public int dimensionId;
    public int scale;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void decodeBody(){
        mapId = readVarLong();
        type = readUnsignedVarInt();
        dimensionId = readByte();
        scale = readByte();

        System.out.println("mapId: " + mapId);
        System.out.println("type: " + type);
        System.out.println("dimensionId: " + dimensionId);
        System.out.println("scale: " + scale);

        if((type & 0x04) != 0){//Decorations
            int trackedEntityCount = readUnsignedVarInt();
            System.out.println("TrackedEntityCount: " + trackedEntityCount);

            for(int i = 0; i < trackedEntityCount; i++){
                int objectType = readIntLE();

                System.out.println("ObjectType: " + objectType);
                if(objectType == 0){
                    int objectEntityId = readVarInt();

                    System.out.println("EntityId: " + objectEntityId);
                }else{
                    int x = readVarInt();
                    int y = readUnsignedVarInt();
                    int z = readVarInt();

                    System.out.println("x: " + x);
                    System.out.println("y: " + y);
                    System.out.println("z: " + z);
                }
            }

            int decorationCount = readUnsignedVarInt();

            System.out.println("DecorationCount: " + decorationCount);
            for(int i = 0; i < decorationCount; i++){
                int decorationType = readByte();
                int decorationRotion = readByte();
                int xOffset = readByte();
                int yOffset = readByte();

                String decorationName = readString();
                int color = readUnsignedVarInt();

                int R = color & 0xff;
                int G = (color >> 8) & 0xff;
                int B = (color >> 16) & 0xff;
                int A = (color >> 24) & 0xff;

                System.out.println("decorationType: " + decorationType);
                System.out.println("decorationRotion: " + decorationRotion);
                System.out.println("xOffset: " + xOffset);
                System.out.println("yOffset: " + yOffset);
                System.out.println("decorationName: " + decorationName);
                System.out.println("color: " + color);
                System.out.println("R: " + R);
                System.out.println("G: " + G);
                System.out.println("B: " + B);
                System.out.println("A: " + A);
            }
        }

        if((type & 0x02) != 0){//Textures
            int width = readVarInt();
            int height = readVarInt();
            int xOffset = readVarInt();
            int yOffset = readVarInt();

            System.out.println("width: " + width);
            System.out.println("height: " + height);
            System.out.println("xOffset: " + xOffset);
            System.out.println("yOffset: " + yOffset);

            int count = readUnsignedVarInt();
            System.out.println("count: " + count);

            int color = readUnsignedVarInt();

            int R = color & 0xff;
            int G = (color >> 8) & 0xff;
            int B = (color >> 16) & 0xff;
            int A = (color >> 24) & 0xff;
            System.out.println("color: " + color);
            System.out.println("R: " + R);
            System.out.println("G: " + G);
            System.out.println("B: " + B);
            System.out.println("A: " + A);
        }

        System.out.println("remaining: " + remaining() + "\n");
    }

}
