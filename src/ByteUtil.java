public class ByteUtil {
    static long nByteToLong(byte[] byteArr)throws ArrayIndexOutOfBoundsException {

        if (byteArr.length > 8){
            throw new ArrayIndexOutOfBoundsException("Array size must be lower than 8byte.");
        }
        long res = 0;
        for (byte b : byteArr){
            res = res << 8 | ByteUtil.makeZeroByte(b);
        }
        return res;
    }

    static long makeZeroByte(byte b){
        long b_long = b;
        b_long = b_long << 56 >>> 56;
        return b_long;
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

    static void printByteArr(byte[] byteArr){
        for (byte b : byteArr){
            System.out.printf("%s ", ByteUtil.hexString(b));
        }
    }
}
