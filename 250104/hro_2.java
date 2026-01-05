class Solution {
    // 이분탐색

    int[] diffs;
    int[] times;
    long limit;

    public int solution(int[] diffs, int[] times, long limit) {
        /*
        diff : 현재 퍼즐의 난이도
        time_cur : 현재 퍼즐의 소요 시간
        time_prev : 이전 퍼즐의 소요 시간
        level : 나의 숙련도

        xxxxx ooooo
        xx ooo ooooo
        xxxxx xx ooo
        
        */
        int answer = 0;
        this.diffs = diffs;
        this.times = times;
        this.limit = limit;
        int n = diffs.length;  // 게임 횟수

        int left = 1;
        int right = 0;
        for(int i=0;i<n;i++){
            right = Math.max(diffs[i], right);  // diffs 배열에서 가장 큰 값
        }

        int answer = right;  // 가장 큰 값 기준으로 이분탐색

        while (left <= right) {
            int mid = (left + right)/2;

            if (isItSolve(mid)) {  // 중간값이 limit보다 작아서 성공했으면
                answer = mid;
                right = mid -1;  // 중간값보다 더 작은 값에 있나 확인
            } else {
                left = mid + 1;  // 중간값보다 더 큰 값에 있나 확인
            }
            
        }

        return answer;
    }

    public boolean isItSolve(int level){
        long totalTime = 0;
        int time_prev = 0;

        for(int i=0;i<diffs.length;i++){
            int diff = diffs[i];         // 현재 퍼즐의 난이도
            int time_cur = times[i];     // 현재 퍼즐을 푸는데 걸리는 시간

            if(diff<=level){
                totalTime += time_cur;
            } else {
                int failCount = diff-level;  // 틀리는 횟수
                totalTime += time_cur + failCount * (time_prev + time_cur);
            }

            if (totalTime > limit) return false;  // 총 시간이 limit보다 크면 실패

            time_prev = time_cur;
        }

        return true; // 모든 퍼즐 성공했으면 true 반환
    }

}