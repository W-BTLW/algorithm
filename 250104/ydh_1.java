class Solution {
    public String solution(String video_len, String pos, String op_start, String op_end, String[] commands) {
        String answer = "";

        // 비디오 최대 길이
        String[] video = video_len.split(":");
        int max_mm = Integer.parseInt(video[0]);
        int max_ss = Integer.parseInt(video[1]);
        int max = max_mm * 60 + max_ss;

        // 현재 비디오 위치
        String[] pos_s = pos.split(":");
        int cur_mm = Integer.parseInt(pos_s[0]);
        int cur_ss = Integer.parseInt(pos_s[1]);
        int cur = cur_mm * 60 + cur_ss;

        // 오프닝 구간
        String[] start = op_start.split(":");
        int op_s_mm = Integer.parseInt(start[0]);
        int op_s_ss = Integer.parseInt(start[1]);
        int op_s = op_s_mm * 60 + op_s_ss;

        // 오프닝 마지막 구간
        String[] end = op_end.split(":");
        int op_e_mm = Integer.parseInt(end[0]);
        int op_e_ss = Integer.parseInt(end[1]);
        int op_e = op_e_mm * 60 + op_e_ss;

        if ((cur >= op_s) && (cur <= op_e)) {
            // 현재 위치가 오프닝 구간이면 오프닝 구간으로 이동
            cur = op_e;
        }

        //System.out.println("cur : " + cur_mm +":"+cur_ss);
        for (String c : commands) {
            if ("next".equals(c)) {
                // 10초 후
                // 10초 추가 했을 때 동영상 길이 이상이면
                if (max - cur < 10) cur = max;
                else cur += 10;
            } else {
                // 10초 뒤
                // -10했을때 0초 이하이면
                if (cur < 10) cur = 0;
                else cur -= 10;
            }

            if ((cur >= op_s) && (cur <= op_e)) {
                // 현재 위치가 오프닝 구간이면 오프닝 구간으로 이동
                cur = op_e;
            }
        }

        int mm = cur / 60;
        int ss = cur % 60;
        String mm_s = ((mm < 10) ? ("0" + mm) : mm) + "";
        String ss_s = ((ss == 0) ? "00" : (ss < 10) ? ("0" + ss) : ss) + "";
        answer = mm_s + ":" + ss_s;
        return answer;
    }
}
