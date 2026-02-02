class Solution {
    
    /**
     * 그리디? 나 절대못풀어
     * 시작할 때 겹쳐있어도 count ++
     * 시작 h1:m1:s1
     * 종료 h2:m2:s2
     * 
     * CASE1
     * 0:5:30 - 0:7:0
     * 
     * CASE2
     * 12:0:0 - 12:0:30
     * 
     * 초당 움직이는 각도
     * 시침 1/120 (360/60*60*12)
     * 분침 1/10 (360/3600)
     * 초침 6 (360/60)
     */

    public int solution(int h1, int m1, int s1, int h2, int m2, int s2) {
        int answer = 0;
        
        // 시작, 종료시간 모두 초로 환산
        int start = (h1*3600) + (m1*60) + s1;
        int end = (h2*3600) + (m2*60) + s2;

        // 시작할 때 겹쳐있으면 미리 카운트
        if (isOverlapped(start)) answer ++;

        // 1초 단위로 초침이 시침/분침을 추월하는지 확인
        for (int t=start;t<end;t++){
            double hCur = getAngle(t, "h");
            double mCur = getAngle(t, "m");
            double sCur = getAngle(t, "s");

            double hNext = getAngle(t+1, "h");
            double mNext = getAngle(t+1, "m");
            double sNext = getAngle(t+1, "s");

            // 다음 초에서 각도가 0인 경우 360으로 변경
            if (hNext == 0) hNext = 360;
            if (mNext == 0) mNext = 360;
            if (sNext == 0) sNext = 360;

            // 초침이 시침 추월했는지 확인
            boolean hCross = sCur<hCur && sNext>=hNext;
            // 초침이 분침 추월했는지 확인
            boolean mCross = sCur<mCur && sNext>=mNext;

            if(hCross && mCross) {
                // 시침,분침 동시에 추월했으면서 시침과 분침의 각도까지 같다면 한 번만 카운트
                if (hNext == mNext) answer += 1;
                else answer += 2;  // 동시에 추월했으면서 각도는 다르다면 2번 카운트
            } else if (hCross || mCross) {
                answer += 1;  // 둘 중 하나만 추월한 경우
            }
        }

        return answer;
    }

    public double getAngle(int totalSecond, String type) {
        if (type.equals("h")) {
            return (totalSecond % (60*60*12)) * 360.0 / (60*60*12);
        } else if (type.equals("m")) {
            return (totalSecond % (60*60)) * 360.0 / (60*60);
        } else {
            return (totalSecond % 60) * 360.0 / 60;
        }
    }

    public boolean isOverlapped(int t){
        return getAngle(t, "s") == getAngle(t, "h") || getAngle(t, "s") == getAngle(t, "m");
    }
}