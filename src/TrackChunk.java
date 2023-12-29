import java.util.Vector;

public class TrackChunk extends Chunk{
    Vector<EventData> event_vec = new Vector<>();
    String track_name = "", copyright = "Unknown", instrument_name, key, scale, lyrics, user_message;
    int tempo, channel, port, numerator, denominator, metronome_ticks, event_state;

    final String[] KEY_ARR = {"Cb","Gb", "Db","Ab","Eb","Bb","Fb","C","G","D","A","E","B","F#","C#"};
    final String[] KEY_ARR2 = {"Ab","Eb", "Bb","Fb","Cb","Gb","Db","A", "E","B","F#","C#","G#","D#","A#"};

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
                    event_state = 0;
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
                        EventData ed = new EventData(dt_arr, tmp_arr, event_state);
                        event_vec.add(ed);
                        k += tmp_arr.length - 2;
                        is_delta_time = true;
                        continue;
                    }
                }
                else if (Byte.toUnsignedInt(data[k]) >= Byte.toUnsignedInt((byte)0xF0) &&
                        Byte.toUnsignedInt(data[k]) <= Byte.toUnsignedInt((byte)0xFE)) {
                    event_state = 1;
                    dt_length = 0;
                    is_delta_time = true;
                    continue;
                }
                else {
                    event_state = 2;
                    tmp_arr = new byte[Byte.toUnsignedInt(data[k + 2]) + 3];
                    System.arraycopy(data, k, tmp_arr, 0, tmp_arr.length);
                }
                EventData ed = new EventData(dt_arr, tmp_arr, event_state);
                event_vec.add(ed);
                k += tmp_arr.length - 1;
                is_delta_time = true;
            }
        }

        for (EventData ed : event_vec){
            if (ed.event_state != 2) continue;
            byte meta_message = ed.data_byte_arr[1];
            switch (meta_message){
                case (byte)0x01:
                    user_message = ByteUtil.makeStringFromByteArray(ed.data_byte_arr, 3, Byte.toUnsignedInt(ed.data_byte_arr[2]));
                    break;
                case (byte)0x02:
                    copyright = ByteUtil.makeStringFromByteArray(ed.data_byte_arr, 3, Byte.toUnsignedInt(ed.data_byte_arr[2]));
                    break;
                case (byte)0x03:
                    track_name = ByteUtil.makeStringFromByteArray(ed.data_byte_arr, 3, Byte.toUnsignedInt(ed.data_byte_arr[2]));
                    break;
                case (byte)0x04:
                    instrument_name = ByteUtil.makeStringFromByteArray(ed.data_byte_arr, 3, Byte.toUnsignedInt(ed.data_byte_arr[2]));
                    break;
                case (byte)0x05:
                    lyrics = ByteUtil.makeStringFromByteArray(ed.data_byte_arr, 3, Byte.toUnsignedInt(ed.data_byte_arr[2]));
                    break;
                case (byte)0x20:
                    channel = Byte.toUnsignedInt(ed.data_byte_arr[3]);
                    break;
                case (byte)0x21:
                    port = Byte.toUnsignedInt(ed.data_byte_arr[3]);
                    break;
                case (byte)0x51:
                    tempo = (int)ByteUtil.nByteToLong(new byte[]{ed.data_byte_arr[3], ed.data_byte_arr[4], ed.data_byte_arr[5]});
                    break;
                case (byte)0x58:
                    numerator = ed.data_byte_arr[3];
                    denominator = (int) Math.pow(2,ed.data_byte_arr[4]);
                    metronome_ticks = ed.data_byte_arr[5];
                    break;
                case (byte)0x59:
                    int sf = Byte.toUnsignedInt(ed.data_byte_arr[3]);
                    if (ed.data_byte_arr[4] == 0){
                        key = KEY_ARR[sf];
                        scale = "major";
                    }
                    else {
                        key = KEY_ARR2[sf];
                        scale = "minor";
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    void printDataInHex(){
        System.out.println("==================================================");
        super.printDataInHex();
        System.out.println("Analyze- - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("Chunk Type : Mtrk");
        System.out.printf("Copyrights : %s\t| Key : %s\t| Scale : %s\t| Tempo : %d/%d\t| Ticks : %d\t| During Tempo : %d%n", copyright, key, scale, numerator, denominator, metronome_ticks, tempo);
        System.out.printf("User Message : %s\t| MIDI Port : %d\t| Channel Prefix : %d\t| Track Name : %s\t| Instrument Name : %s%n", user_message, port, channel, track_name, instrument_name);
        System.out.printf("Lyrics : %s%n%n", lyrics);
        for (EventData ed : event_vec){
            ed.print();
        }
    }
}
