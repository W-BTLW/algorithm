class Solution {
    public String solution(String video_len, String pos, String op_start, String op_end, String[] commands) {
        String answer = "";

        int iVideo = chToInt(video_len);
        int iPos = chToInt(pos);
        int iOpStart = chToInt(op_start);
        int iOpEnd = chToInt(op_end);


        for(int i=0;i<commands.length;i++){
            if (iPos <= iOpEnd && iPos >= iOpStart){
                iPos = iOpEnd;
            }
            if (commands[i].equals("next")){
                if (iPos+10 >= iVideo) {
                    iPos = iVideo;
                } else {
                    iPos += 10;
                }
            } else if (commands[i].equals("prev")){
                if (iPos-10 <= 0) {
                    iPos = 0;
                } else {
                    iPos -= 10;
                }
            }
        }
        if (iPos <= iOpEnd && iPos >= iOpStart){
                iPos = iOpEnd;
            }

        answer = chToString(iPos);

        return answer;
    }


    public int chToInt(String str){
        int result = 0;
        String[] timeByStr = new String[2];
        int minute = 0;
        int second = 0;

        timeByStr = str.split(":");
        minute = Integer.parseInt(timeByStr[0]);
        second = Integer.parseInt(timeByStr[1]);

        result = minute * 60 + second;
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