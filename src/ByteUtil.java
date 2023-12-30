import java.nio.charset.StandardCharsets;

public class ByteUtil {
    static long nByteToLong(byte[] byte_arr)throws ArrayIndexOutOfBoundsException {

        if (byte_arr.length > 8){
            throw new ArrayIndexOutOfBoundsException("Array size must be lower than 8byte.");
        }
        long res = 0;
        for (byte b : byte_arr){
            res = res << 8 | (b & (long)255);
        }
        return res;
    }

    static String toBinaryString(long i){
        String zero = "0000000000000000000000000000000000000000000000000000000000000000";
        String tmp = zero + Long.toBinaryString(i);
        tmp = tmp.substring(tmp.length()-64);

        return tmp;
    }

    static String hexString(byte b){
        return Integer.toString(((int) b & 0xff)+0x100, 16).substring(1).toUpperCase();
    }

    static void printByteArr(byte[] byte_arr){
        for (byte b : byte_arr){
            System.out.printf("%s ", ByteUtil.hexString(b));
        }
    }

    static String makeStringFromByteArray(byte[] byte_arr, int start_index, int size){
        byte[] tmp = new byte[size];
        System.arraycopy(byte_arr, start_index, tmp, 0, size);
        String res = new String(tmp, StandardCharsets.UTF_8);
        return res;
    }
}
