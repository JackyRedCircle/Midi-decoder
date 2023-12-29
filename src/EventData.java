public class EventData {
    int delta_time, event_state; //event_state: 0(midi event), 1(system event), 2(metadata)
    byte[] data_byte_arr, dt_byte_arr;

    EventData(byte[] dt_byte_arr, byte[] metadata_byte_arr, int event_state){
        this.data_byte_arr = metadata_byte_arr; this.dt_byte_arr = dt_byte_arr;
        delta_time = 0;
        for (byte b : this.dt_byte_arr){
            delta_time = delta_time << 8 | ((int)ByteUtil.makeZeroByte(b) << 1) >>> 1;
        }
        this.event_state = event_state;
    }

    void print(){
        System.out.print("Delta Time in Hex : ");
        ByteUtil.printByteArr(dt_byte_arr);
        System.out.println();
        System.out.print("Event data in Hex : ");
        ByteUtil.printByteArr(data_byte_arr);
        System.out.println();
        System.out.printf("Event data [delta time : %d | message : %s]%n%n", delta_time, ByteUtil.hexString(data_byte_arr[1]));
    }


}
