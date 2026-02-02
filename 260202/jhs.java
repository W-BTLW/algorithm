class Solution {

    public static double calc(double x){
        // % 360에 의해 0이 되어도 360이 되도록 만들기
        if (x == 0){
            return 360;
        }
        return x;
    }

    public int solution(int h1, int m1, int s1, int h2, int m2, int s2) {
        
        // 움직이는 각도
        // 침 : 1분에 한바퀴 -> 분당 360도, 초당 6도
        // 분침 : 1시간에 한바퀴 -> 시간당 360도, 분당 6도, 초당 0.1 
        // 시침 : 12시간에 한바퀴 -> 시간당 30도, 분당 0.5도, 초당 1/120도
        
        int answer = 0;

        // 초로 환산
        double startTime = (h1*3600) + (m1*60) + s1;
        double endTime = (h2*3600) + (m2*60) + s2;

        // 시작부터 다 겹쳐있는경우 체크 00:00:00 or 12:00:00
        if (startTime == 0 || startTime == 12 * 3600) {
            answer += 1;
        }
        while (startTime < endTime) {
            // 현재초의 각도 구하기 
            // 각 시분초침에 맞게 초당 
            double nowH = startTime / 120 % 360;
            double nowM = startTime / 10 % 360;
            double nowS = startTime * 6 % 360;

            // 다음초의 각도 구하기
            double nextH = calc((startTime + 1) / 120 % 360);
            double nextM = calc((startTime + 1) / 10 % 360);
            double nextS = calc((startTime + 1) * 6 % 360);

            // 초침이 시침or분침을 넘어가는 순간을 포착
            if (nowS < nowH && nextH <= nextS) { // 초침-시침
                answer += 1;
            }
            if (nowS < nowM && nextM <= nextS) { // 초침-분침
                answer += 1;
            }    
            if (nextS == nextH && nextH == nextM) { // 초침, 시침, 분침이 모두 겹치면 위에서 2번 카운팅 되므로
                answer -= 1;
            }

            startTime += 1;
        }

        return answer;
    }
}
