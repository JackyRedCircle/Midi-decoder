abstract public class Chunk {
    byte[] type, length, data;
    int data_length;

    Chunk(byte[] type, byte[] length, byte[] data){
        this.type = type; this.length = length; this.data = data;
        this.data_length = data.length;
    }

    abstract void printDataInHex();

}
