import java.util.Vector;

public class TrackChunk extends Chunk{
    Vector<EventData> event_vec = new Vector<>();

    TrackChunk(byte[] type, byte[] length, byte[] data){
        super(type, length,data);
        boolean is_delta_time = true;
        int dt_length = 0;
        byte[] dt_arr = null, tmp_arr, prev_midi_data_arr = null;
        for(int k = 0; k < data.length; k++){
            if (is_delta_time) {
                dt_length++;
                if ((int)data[k] >= 0) {
                    dt_arr = new byte[dt_length];
                    System.arraycopy(data, k - dt_length+1, dt_arr, 0, dt_length);
                    is_delta_time = false;
                    dt_length = 0;
                }
            }
            else{
                if (Byte.toUnsignedInt(data[k]) <= Byte.toUnsignedInt((byte)0xEF)) {
                    if (Byte.toUnsignedInt(data[k]) >= Byte.toUnsignedInt((byte)0x80) &&
                            Byte.toUnsignedInt(data[k]) <= Byte.toUnsignedInt((byte)0xBF) ||
                            Byte.toUnsignedInt(data[k]) >= Byte.toUnsignedInt((byte)0xE0) &&
                                    Byte.toUnsignedInt(data[k]) <= Byte.toUnsignedInt((byte)0xEF)){
                        tmp_arr = new byte[3];
                        System.arraycopy(data, k, tmp_arr, 0, 3);
                        prev_midi_data_arr = tmp_arr;
                    }
                    else if (Byte.toUnsignedInt(data[k]) >= Byte.toUnsignedInt((byte)0xC0) &&
                                Byte.toUnsignedInt(data[k]) <= Byte.toUnsignedInt((byte)0xDF)){
                        tmp_arr = new byte[2];
                        System.arraycopy(data, k, tmp_arr, 0, 2);
                        prev_midi_data_arr = tmp_arr;
                    }
                    else {
                        assert prev_midi_data_arr != null;
                        tmp_arr = new byte[prev_midi_data_arr.length];
                        tmp_arr[0] = prev_midi_data_arr[0];
                        System.arraycopy(data, k, tmp_arr, 1, tmp_arr.length - 1);
                        MidiEvent me = new MidiEvent(dt_arr, tmp_arr, true);
                        event_vec.add(me);
                        k += tmp_arr.length - 2;
                        is_delta_time = true;
                        continue;
                    }
                    MidiEvent me = new MidiEvent(dt_arr, tmp_arr, false);
                    event_vec.add(me);
                }
                else if (Byte.toUnsignedInt(data[k]) >= Byte.toUnsignedInt((byte)0xF0) &&
                        Byte.toUnsignedInt(data[k]) <= Byte.toUnsignedInt((byte)0xFE)) {
                    dt_length = 0;
                    is_delta_time = true;
                    continue;
                }
                else {
                    tmp_arr = new byte[Byte.toUnsignedInt(data[k + 2]) + 3];
                    System.arraycopy(data, k, tmp_arr, 0, tmp_arr.length);
                    Metadata md = new Metadata(dt_arr, tmp_arr);
                    event_vec.add(md);
                }
                k += tmp_arr.length - 1;
                is_delta_time = true;
            }
        }
    }

    @Override
    void printDataInHex(){
        System.out.println("==================================================");
        System.out.println("Chunk Type : Mtrk");
        for (EventData ed : event_vec){
            ed.print();
//            System.out.print("\t\t\t\tdelT hex\t\t\t: ");
//            ByteUtil.printByteArr(ed.getDeltaTimeArr());
//            System.out.println();
//            System.out.print("\t\t\t\tdata hex\t\t\t: ");
//            ByteUtil.printByteArr(ed.getDataArr());
//            System.out.println();
//            ed.printStateInHex();
        }
    }
}
