package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

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
        scale = readByte();

        /*System.out.println("mapId: " + mapId);
        System.out.println("type: " + type);
        System.out.println("dimensionId: " + dimensionId);
        System.out.println("scale: " + scale);*/

        if((type & 0x04) != 0){//Decorations
            int trackedEntityCount = readUnsignedVarInt();
            //System.out.println("TrackedEntityCount: " + trackedEntityCount);

            for(int i = 0; i < trackedEntityCount; i++){
                int objectType = readIntLE();

                //System.out.println("ObjectType: " + objectType);
                if(objectType == 0){
                    int objectEntityId = readVarInt();

                    //System.out.println("EntityId: " + objectEntityId);
                }else{
                    int x = readVarInt();
                    int y = readUnsignedVarInt();
                    int z = readVarInt();

                    //System.out.println("x: " + x);
                    //System.out.println("y: " + y);
                    //System.out.println("z: " + z);
                }
            }

            int decorationCount = readUnsignedVarInt();

            //System.out.println("DecorationCount: " + decorationCount);
            for(int i = 0; i < decorationCount; i++){
                int decorationType = readByte();
                int decorationRotation = readByte();
                int xOffset = readByte();
                int yOffset = readByte();

                String decorationName = readString();
                int color = readUnsignedVarInt();

                int R = color & 0xff;
                int G = (color >> 8) & 0xff;
                int B = (color >> 16) & 0xff;
                int A = (color >> 24) & 0xff;

                /*System.out.println("decorationType: " + decorationType);
                System.out.println("decorationRotation: " + decorationRotation);
                System.out.println("xOffset: " + xOffset);
                System.out.println("yOffset: " + yOffset);
                System.out.println("decorationName: " + decorationName);
                System.out.println("color: " + color);
                System.out.println("R: " + R);
                System.out.println("G: " + G);
                System.out.println("B: " + B);
                System.out.println("A: " + A);*/
            }
        }

        if((type & 0x02) != 0){//Textures
            int width = readVarInt();
            int height = readVarInt();
            int xOffset = readVarInt();
            int yOffset = readVarInt();

            /*System.out.println("width: " + width);
            System.out.println("height: " + height);
            System.out.println("xOffset: " + xOffset);
            System.out.println("yOffset: " + yOffset);*/

            int count = readUnsignedVarInt();
            //System.out.println("count: " + count);

            colors = new int[height][width];
            for(int y = 0; y < height; ++y){
                for(int x = 0; x < width; ++x){
                    colors[y][x] = readUnsignedVarInt();
                }
            }

            try{
                BufferedImage bufferedImage;
                if(new File(mapId + ".png").exists()){
                    bufferedImage = ImageIO.read(new File(mapId + ".png"));
                }else{
                    bufferedImage = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);
                }

                for(int y = 0; y < height; ++y){
                    int drawY = y + yOffset;
                    for(int x = 0; x < width; ++x){
                        int drawX = x + xOffset;
                        int color = colors[y][x];
                        int R = color & 0xff;
                        int G = (color >> 8) & 0xff;
                        int B = (color >> 16) & 0xff;
                        int A = (color >> 24) & 0xff;
                        int drawRGBA = A << 24 | R << 16 | G << 8 | B;

                        bufferedImage.setRGB(drawX, drawY, drawRGBA);
                    }
                }

                ImageIO.write(bufferedImage, "png", new File(mapId +".png"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        System.out.println("remaining: " + remaining() + "\n");
    }

}
