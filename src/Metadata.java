public class Metadata extends EventData {
    private final byte[] data_byte_arr;//메타데이터 중 실질적인 데이터 배열 ex) FF 01 08 [ ~~ ] (대괄호 내용)
    public final int data_length;
    private final String[] KEY_ARR = {"Cb","Gb", "Db","Ab","Eb","Bb","Fb","C","G","D","A","E","B","F#","C#"};
    private final String[] KEY_ARR2 = {"Ab","Eb", "Bb","Fb","Cb","Gb","Db","A", "E","B","F#","C#","G#","D#","A#"};
    Metadata(byte[] delta_time_byte_arr, byte[] data_byte_arr){
        super(delta_time_byte_arr, data_byte_arr);
        state_byte = super.data_byte_arr[1];
        this.data_length = Byte.toUnsignedInt(super.data_byte_arr[2]);
        this.data_byte_arr = new byte[super.data_byte_arr.length - 3];
        System.arraycopy(super.data_byte_arr, 3, this.data_byte_arr, 0, this.data_byte_arr.length);
        switch (state_byte){
            case (byte)0x01:
                str_data_type = "User text";
                break;
            case (byte)0x02:
                str_data_type = "Copyright info";
                break;
            case (byte)0x03:
                str_data_type = "Track name";
                break;
            case (byte)0x04:
                str_data_type = "Instrument name";
                break;
            case (byte)0x05:
                str_data_type = "Lyrics";
                break;
            case (byte)0x20:
                str_data_type = "Channel prefix";
                break;
            case (byte)0x21:
                str_data_type = "MIDI port";
                break;
            case (byte)0x51:
                str_data_type = "Tempo (microsecond/quarter note)";
                break;
            case (byte)0x58:
                str_data_type = "Time signature";
                break;
            case (byte)0x59:
                str_data_type = "Key signature";
                break;
            case (byte)0x2f:
                str_data_type = "End of track chunk";
                break;
            default:
                break;
        }
    }

    void print(){
        System.out.println("----------------------------------");
        System.out.printf("<Metadata>\t\tdelta time\t: %d%n", this.delta_time);
        super.print();
        switch (state_byte){
            case (byte)0x01:
                System.out.printf("\t\t\t\tuser text\t: %s%n", ByteUtil.makeStringFromByteArray(data_byte_arr, 0, data_length));
                break;
            case (byte)0x02:
                System.out.printf("\t\t\t\tcopyright\t\t: %s%n", ByteUtil.makeStringFromByteArray(data_byte_arr, 0, data_length));
                break;
            case (byte)0x03:
                System.out.printf("\t\t\t\ttrack name\t: %s%n", ByteUtil.makeStringFromByteArray(data_byte_arr, 0, data_length));
                break;
            case (byte)0x04:
                System.out.printf("\t\t\t\tins. name\t\t: %s%n", ByteUtil.makeStringFromByteArray(data_byte_arr, 0, data_length));
                break;
            case (byte)0x05:
                System.out.printf("\t\t\t\tlyrics   \t\t: %s%n",ByteUtil.makeStringFromByteArray(data_byte_arr, 0, data_length));
                break;
            case (byte)0x20:
                System.out.printf("\t\t\t\tchannel prefix  : %d%n", Byte.toUnsignedInt(data_byte_arr[0]));
                break;
            case (byte)0x21:
                System.out.printf("\t\t\t\tMidi port \t: %d%n", Byte.toUnsignedInt(data_byte_arr[0]));
                break;
            case (byte)0x51:
                System.out.printf("\t\t\t\tunit tempo\t: %d%n", (int)ByteUtil.nByteToLong(new byte[]{data_byte_arr[0], data_byte_arr[1], data_byte_arr[2]}));
                break;
            case (byte)0x58:
                int numerator = data_byte_arr[0];
                int denominator = (int) Math.pow(2,Byte.toUnsignedInt(data_byte_arr[1]));
                int metronome_ticks = Byte.toUnsignedInt(data_byte_arr[2]);
                System.out.printf("\t\t\t\ttempo     \t: %d/%d%n", numerator, denominator);
                System.out.printf("\t\t\t\tmetronome \t: %d%n", metronome_ticks);

                break;
            case (byte)0x59:
                String key, scale;
                int sf = Byte.toUnsignedInt(data_byte_arr[0]);
                if (data_byte_arr[1] == 0){
                    key = KEY_ARR[sf];
                    scale = "major";
                }
                else {
                    key = KEY_ARR2[sf];
                    scale = "minor";
                }
                System.out.printf("\t\t\t\tkey   \t\t: %s%n", key);
                System.out.printf("\t\t\t\tscale \t\t: %s%n", scale);
                break;
            default:
                break;
        }
    }

    byte[] getDataArr(){
        return this.data_byte_arr;
    }
}
