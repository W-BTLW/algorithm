import java.util.*;

class Solution {

    private static final int MOVE_SECONDS = 10;

    public String solution(String video_len, String pos, String op_start, String op_end, String[] commands) {
        String answer = "";
        int video_len_int = timeToSec(video_len);
        int pos_int = timeToSec(pos);
        int op_start_int = timeToSec(op_start);
        int op_end_int = timeToSec(op_end);
        
        for (int i = 0; i < commands.length; i++) {

            if (pos_int >= op_start_int && pos_int <= op_end_int) {
                pos_int = op_end_int;
            }

            if ("prev".equals(commands[i])) {
                pos_int = Math.max(0, pos_int - MOVE_SECONDS);
            } else {
                pos_int = Math.min(video_len_int, pos_int + MOVE_SECONDS);
            }

            if (pos_int >= op_start_int && pos_int <= op_end_int) {
                pos_int = op_end_int;
            }
        }

        answer = timeToStr(pos_int);

        return answer;
    }

    public int timeToSec(String timeStr) {
        String[] parts = timeStr.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public String timeToStr(int timeInt) {
        String min = Integer.toString(timeInt / 60);
        String sec = Integer.toString(timeInt % 60);

        if (min.length() == 1) {
            min = '0' + min;
        }

        if (sec.length() == 1) {
            sec = '0' + sec;
        }

        return min + ':' + sec;
    }
}
