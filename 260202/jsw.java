// Testcase 6 out of 7
// 매초 시뮬레이션하여 초침이 분침 or 시침을 지났을 때 알람을 count
// 오답(Feedback needed)

class Solution {

    static final int CYCLE = 43200;
    static final int HOUR_MOVE = 1;
    static final int MIN_MOVE = 12;
    static final int SEC_MOVE = 720;

    public int solution(int h1, int m1, int s1, int h2, int m2, int s2) {
        int start = toSeconds(h1, m1, s1);
        int end = toSeconds(h2, m2, s2);

        int hour = start;
        int minute = (start * 12) % CYCLE;
        int second = (start * 720) % CYCLE;

        int answer = 0;

        // 시작 시점 겹침 (3침이든 2침이든 1번)
        if (isOverlap(hour, minute, second)) {
            answer++;
        }

        for (int t = start; t < end; t++) {
            int nh = (hour + HOUR_MOVE) % CYCLE;
            int nm = (minute + MIN_MOVE) % CYCLE;
            int ns = (second + SEC_MOVE) % CYCLE;

            int cnt = 0;

            if (cross(second, ns, minute)) cnt++;
            if (cross(second, ns, hour)) cnt++;

            // 3침 완전 겹침은 1번만
            if (ns == nm && nm == nh) {
                cnt = 1;
            }

            answer += cnt;

            hour = nh;
            minute = nm;
            second = ns;
        }

        return answer;
    }

    private boolean isOverlap(int h, int m, int s) {
        return h == m || h == s || m == s;
    }

    private boolean cross(int before, int after, int target) {
        if (before == target) return false;

        if (before < after) {
            return before < target && target <= after;
        } else {
            return before < target || target <= after;
        }
    }

    private int toSeconds(int h, int m, int s) {
        return h * 3600 + m * 60 + s;
    }
}
