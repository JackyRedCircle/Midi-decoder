import java.io.ByteArrayOutputStream;

public class MidiEvent extends EventData {
    public final int channel, data_length;
    public final byte midi_state;
    private final byte[] data_byte_arr;
    public final boolean is_running_status;
    MidiEvent(byte[] delta_time_byte_arr, byte[] data_byte_arr, boolean is_running_status){
        super(delta_time_byte_arr, data_byte_arr);
        this.is_running_status = is_running_status;
        state_byte = data_byte_arr[0];
        channel = state_byte & 0x0000000f;
        midi_state = (byte)(state_byte >>> 4 & 0x0000000f);
        switch (midi_state){
            case (byte)0x08:
                str_data_type = "Note off";
                break;
            case (byte)0x09:
                str_data_type = "Note on";
                break;
            case (byte)0x0a:
                str_data_type = "Key after touch";
                break;
            case (byte)0x0b:
                str_data_type = "Control change";
                break;
            case (byte)0x0c:
                str_data_type = "Program change";
                break;
            case (byte)0x0d:
                str_data_type = "Channel after touch";
                break;
            case (byte)0x0e:
                str_data_type = "Pitch wheel change";
                break;
            default:
                break;
        }
        if(this.is_running_status) str_data_type = "Running status";
        this.data_length = super.data_byte_arr.length - 1;
        this.data_byte_arr = new byte[data_length];
        System.arraycopy(super.data_byte_arr, 1, this.data_byte_arr, 0, data_length);
    }

    void print(){
        System.out.println("----------------------------------");
        System.out.printf("<MIDI Event>\tdelta time\t: %d%n", this.delta_time);
        super.print();
        System.out.printf("\t\t\t\tchannel\t\t: %d%n", this.channel);
    }

    void printStateInHex(){
        super.printStateInHex();
    }

    byte[] getDataArr(){
        return this.data_byte_arr;
    }
}
