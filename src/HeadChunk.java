public class HeadChunk extends Chunk{
    byte[] format_byte_arr, track_cnt_byte_arr, division_byte_arr;
    int format, track_count, division;

    HeadChunk(byte[] type, byte[] length, byte[] data) {
        super(type, length, data);
        format_byte_arr = new byte[]{data[0], data[1]};
        format = (int)ByteUtil.nByteToLong(format_byte_arr);
        track_cnt_byte_arr = new byte[]{data[2], data[3]};
        track_count = (int)ByteUtil.nByteToLong(track_cnt_byte_arr);
        division_byte_arr = new byte[]{data[4], data[5]};
        division = (int)ByteUtil.nByteToLong(division_byte_arr);
    }

    @Override
    void printDataInHex(){
        System.out.println("==================================================");
        super.printDataInHex();
        System.out.println("Analyze- - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("Chunk Type : Mthd");
        System.out.printf("Chunk Format : %d%n", format);
        System.out.printf("Chunk Track count : %d%n", track_count);
        System.out.printf("Chunk Division : %d%n", division);
    }
}
