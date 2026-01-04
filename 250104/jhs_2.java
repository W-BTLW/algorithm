import java.util.*;

class Solution {
    public int solution(int[] diffs, int[] times, long limit) {
        int answer = 0;
        int start = 1;
        int end = Arrays.stream(diffs).max().getAsInt();
        int mid_level = 0;
        
        while(start <= end) {
            mid_level = (start + end) / 2;

            boolean result = solvePuzzle(diffs, times, limit, mid_level);

            if (result) {
                answer = (int) mid_level;
                end = mid_level - 1;
            } else {
                start = mid_level + 1;
            }
        }

        return answer;
    }

    public boolean solvePuzzle(int[] diffs, int[] times, long limit, int mid_level) {
        long totalTime = 0;
        int prevTime = 0;
        for (int i = 0; i < diffs.length; i ++) {
            int diff = diffs[i];
            int time = times[i];
            
            if(diff <= mid_level) {
                totalTime += (long)time;
            } else {
                prevTime = (i > 0) ? times[i - 1] : 0;
                totalTime += (long)(diff - mid_level)*(prevTime + time) + time;   
            }
            if (totalTime > limit) return false;
        }

        if (totalTime <= limit) {
            return true;
        }
        return false;
    }
}
