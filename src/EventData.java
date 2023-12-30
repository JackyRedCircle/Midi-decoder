public class EventData {
    public int data_length, delta_time; //event_state: 0(midi event), 1(system event), 2(metadata)
    public byte state_byte;
    public String str_data_type; //각 데이터가 나타내는 내용을 담은 문자열 ex) User text, Tempo 등
    protected final byte[] data_byte_arr, delta_time_byte_arr;

    EventData(byte[] delta_time_byte_arr, byte[] data_byte_arr){
        this.data_byte_arr = data_byte_arr;
        this.delta_time_byte_arr = delta_time_byte_arr;
        this.state_byte = 0;
        this.str_data_type = null;
        this.data_length = data_byte_arr.length;
        delta_time = 0;
        for (byte b : this.delta_time_byte_arr){
            delta_time = delta_time << 8 | ((b & 0x000000ff) << 1) >>> 1;
        }
    }

    void print(){
        System.out.printf("\t\t\t\tmessage\t\t: %s%n", this.str_data_type);
    }

    byte[] getDataArr(){
        return this.data_byte_arr;
    }

    byte[] getDeltaTimeArr(){
        return this.delta_time_byte_arr;
    }

    void printStateInHex(){
        System.out.printf("\t\t\t\tmessage in hex\t\t: %s%n", ByteUtil.hexString(data_byte_arr[0]));
    }
}
