public class Chunk {
    byte[] type, length, data;
    int data_length;

    Chunk(byte[] type, byte[] length, byte[] data){
        this.type = type; this.length = length; this.data = data;
        this.data_length = data.length;
    }

    void printDataInHex(){
        System.out.print("Chunk Type : ");
        ByteUtil.printByteArr(type);
        System.out.printf("%nChunk Length[ %d bytes ] : ", data_length);
        ByteUtil.printByteArr(length);
        System.out.printf("%nChunk Data : ");
        ByteUtil.printByteArr(data);
        System.out.println();
    }

}
