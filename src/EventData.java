public class EventData {
    int delta_time, dt_length;
    byte[] metadata_byte_arr, dt_byte_arr;

    EventData(byte[] dt_byte_arr, byte[] metadata_byte_arr){
        this.metadata_byte_arr = metadata_byte_arr; this.dt_byte_arr = dt_byte_arr;
        for (byte b : this.dt_byte_arr){
            delta_time = delta_time << 8 | (b << 1) >>> 1;
        }
    }

    void print(){
        System.out.printf("Metadata in Hex : ");
        ByteUtil.printByteArr(metadata_byte_arr);
        System.out.println();
        System.out.printf("Metadata [delta time : %d | message : %s]%n%n", delta_time, ByteUtil.hexString(metadata_byte_arr[1]));
    }


}
