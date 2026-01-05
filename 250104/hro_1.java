class Solution {
    public String solution(String video_len, String pos, String op_start, String op_end, String[] commands) {
        /*
        video_len : 동영상의 길이
        pos : 기능이 수행되기 직전의 재생 위치
        op_start : 오프닝 시작 시각
        op_end : 오프닝이 끝나는 시각
        commands : 사용자 입력 ex) prev, next
        */
        
        String answer = "";

        int iVideo = chToInt(video_len);
        int iPos = chToInt(pos);
        int iOpStart = chToInt(op_start);
        int iOpEnd = chToInt(op_end);


        for(int i=0;i<commands.length;i++){
            if (iPos <= iOpEnd && iPos >= iOpStart){  // 현 시점이 오프닝중이면
                iPos = iOpEnd;                        // 오프닝이 끝나는 위치로
            }
            if (commands[i].equals("next")){
                if (iPos+10 >= iVideo) {  // 동영상 길이를 넘어서면
                    iPos = iVideo;        // 동영상 마지막 위치로
                } else {
                    iPos += 10;          // 아니면 현재위치에서 10초 증가
                }
            } else if (commands[i].equals("prev")){
                if (iPos-10 <= 0) {      // 동영상 시작길이보다 작으면
                    iPos = 0;            // 동영상 시작 위치로
                } else {
                    iPos -= 10;          // 아니면 동영상 처음 위치로
                }
            }
        }
        if (iPos <= iOpEnd && iPos >= iOpStart){  // 마지막 위치가 오프닝중이면
                iPos = iOpEnd;                    // 오프닝이 끝나는 위치로
            }

        answer = chToString(iPos);

        return answer;
    }


    public int chToInt(String str){  //초단위로 변환
        int result = 0;
        String[] timeByStr = new String[2];
        int minute = 0;
        int second = 0;

        timeByStr = str.split(":");
        minute = Integer.parseInt(timeByStr[0]);
        second = Integer.parseInt(timeByStr[1]);

        result = minute * 60 + second;  // ex) 13:00 -> 13*60 + 00 = 780
        return result;
    }

    public String chToString(int time){
        String result = "";
        int minute = time/60;
        int second = time%60;
        String sMin = minute+"";
        String sSec = second+"";

        if(minute<10) sMin = "0"+minute;
        if(second<10) sSec = "0"+second;

        result = sMin + ":" + sSec;
        return result;
    }
}