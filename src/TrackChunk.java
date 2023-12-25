import java.util.Arrays;
import java.util.Vector;

public class TrackChunk extends Chunk{
    Vector<EventData> event_vec = new Vector<>();
    String chunk_name = "", copyright = "Unknown", instrument_name = "", key, scale;
    int tempo, channel, port, numerator, denominator, metronome_ticks;

    final String[] KEY_ARR = {"Cb","Gb", "Db","Ab","Eb","Bb","Fb","C","G","D","A","E","B","F#","C#"};
    final String[] KEY_ARR2 = {"Ab","Eb", "Bb","Fb","Cb","Gb","Db","A", "E","B","F#","C#","G#","D#","A#"};

    TrackChunk(byte[] type, byte[] length, byte[] data){
        super(type, length,data);
        System.out.println("트랙 청크 생성 시작 -------");
        boolean is_delta_time = true;
        int dt_length = 0;
        byte[] dt_arr, meta_tmp_arr;
        for(int k = 0; k < data.length; k++){
            if (is_delta_time) {
                System.out.println("델타 타임 체크 중 -------");
                dt_length++;
                if (data[k] > 0) {
                    is_delta_time = false;
                    System.out.println("델타 타임 종료 -------");
                }
            }
            else{
                System.out.println("데이터 해석 시작 -------");
                dt_arr = new byte[dt_length];
                System.arraycopy(data, k - dt_length, dt_arr, 0, dt_length);
                if (data[k] == (byte)0xff){
                    System.out.println("메타 데이터 해석 중 -------");
                    byte[] tmp_arr;
                    if (data[k + 1] == 0x2F) {
                        meta_tmp_arr = new byte[2];
                        meta_tmp_arr[0] = data[k];
                        meta_tmp_arr[1] = data[k+1];
                        /*트랙 청크 종료*/
                    }
                    else {
                        System.out.println("다른 데이터 해석 중 -------");
                        meta_tmp_arr = new byte[Byte.toUnsignedInt(data[k + 2]) + 3];
                        System.arraycopy(data, k, meta_tmp_arr, 0, meta_tmp_arr.length);
                    }
                }
                else {
                    meta_tmp_arr = new byte[0];
                    System.out.println("이도 저도 아닌 무언가가 실행됨");
                }
                EventData ed = new EventData(dt_arr, meta_tmp_arr);
                event_vec.add(ed);
                k += meta_tmp_arr.length;
                is_delta_time = true;
            }
        }

        for (EventData ed : event_vec){
            byte message = ed.metadata_byte_arr[1];
            switch (message){
                case (byte)0x21:

                case (byte)0x51:
                    tempo = (int)ByteUtil.nByteToLong(new byte[]{ed.metadata_byte_arr[3], ed.metadata_byte_arr[4], ed.metadata_byte_arr[5]});
                    break;
                case (byte)0x58:
                    numerator = ed.metadata_byte_arr[3];
                    denominator = (int) Math.pow(2,ed.metadata_byte_arr[4]);
                    metronome_ticks = ed.metadata_byte_arr[5];
                    break;
                case (byte)0x59:
                    int sf = Byte.toUnsignedInt(ed.metadata_byte_arr[3]);
                    if (ed.metadata_byte_arr[4] == 0){
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
        System.out.printf("Key : %s | scale : %s | tempo : %d/%d | ticks : %d | during tempo : %d%n", key, scale, numerator, denominator, metronome_ticks, tempo);
        for (EventData ed : event_vec){
            ed.print();
        }
    }
}
