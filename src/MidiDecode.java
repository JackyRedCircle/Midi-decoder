import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class MidiDecode {
    public static void main(String[] args) {
        Vector<Chunk> cv = new Vector<>();
        final byte[] mthd_code = {0x4D, 0x54, 0x68, 0x64};
        try (FileInputStream fis = new FileInputStream("C:\\Users\\jykim\\MuseScore\\practice1.mid")) {
            while (fis.available() > 0){
                byte[] type_byte_arr = new byte[4], size_byte_arr = new byte[4], data_byte_arr;
                fis.read(type_byte_arr);
                fis.read(size_byte_arr);
                data_byte_arr = new byte[(int)ByteUtil.nByteToLong(size_byte_arr)];
                fis.read(data_byte_arr);
                if (Arrays.equals(type_byte_arr, mthd_code)){
                    HeadChunk hc = new HeadChunk(type_byte_arr, size_byte_arr, data_byte_arr);
                    cv.add(hc);
                }
                else {
                    TrackChunk  tc = new TrackChunk(type_byte_arr, size_byte_arr, data_byte_arr);
                    cv.add(tc);
                }
            }
        }catch (IOException e){
            System.out.println("path exception");
        }

        for (Chunk c : cv) {
            c.printDataInHex();
        }
    }
}
