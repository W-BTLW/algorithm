class Solution {
    public int solution(int[] diffs, int[] times, long limit) {
        int answer = 0;

        // 숙련도의 최솟값 찾아야하므로 전방위 탐색은 안됨
        // 숙련도 1이랑 가장 어려운퍼즐 한번에 할수 있는놈 찾기
        // 그리고 중간값으로 가지치기 > 이진탐색 필요

        // 제일 높은 난이도에 대한 숙련도 구해보자
        int end = 0;
        for(int i=0; i<diffs.length; i++) {
            if (diffs[i] > end) end = diffs[i];
        }

        int start = 1;
        int mid = 0;

        while (start <= end) {
            mid = (start + end) / 2;

            boolean isPlay = playPuzzle(diffs, times, limit, mid);

            // 최소값 찾아가기 위한 여정
            if (isPlay) {
                // 지금 숙련도로 성공했으니까 이 중간값 밑으로 찾아서 다시 한번
                answer = mid;
                end = mid - 1;
            } else {
                // 숙련도 낮아서 실패했으니까 이걸론 안되니까 시작점 올려서 다시 한번
                start = mid + 1;
            }
        }

        return answer;
    }

    public boolean playPuzzle(int[] diffs, int[] times, long limit, int level) {
        // 10^15까지라서 int형 안되무 ㅠ
        long playTime = 0;

        for(int i=0; i<diffs.length; i++) {
            int diff = diffs[i]; // 현재 퍼즐의 난이도
            int time_cur = times[i]; // 현재 퍼즐의 소요 시간

            if (diff <= level) {
                // 틀리지 않고 time_cur만큼의 시간을 사용하여 해결
                playTime += time_cur;
            } else {
                // diff - level번 틀림, 틀릴때 마다 time_cur만큼 시간을 사용하여 추가로 time_prev만큼 시간 사용해 이전 퍼즐 다시 풀고와야함
                int doCnt = diff - level; // 틀리는 횟수
                int time_prev = (i == 0) ? 0 : times[i-1];
                playTime += (time_cur + time_prev) * doCnt + time_cur; // 틀릴때 마다 time_cur만큼 시간을 사용하여 추가로 time_prev만큼 시간 사용해 이전 퍼즐 다시 풀고와야함
            }

            // 총 시간이 limit보다 크면 실패임
            if (playTime > limit) return false;
        }

        return true;
    }
}
