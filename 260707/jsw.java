import java.util.*;

class Solution {

    static class Program {
        int score;      // 우선순위
        int callTime;   // 호출 시각
        int runTime;    // 실행 시간

        Program(int score, int callTime, int runTime) {
            this.score = score;
            this.callTime = callTime;
            this.runTime = runTime;
        }
    }

    public long[] solution(int[][] program) {

        // 호출 시간 순
        PriorityQueue<Program> callPQ = new PriorityQueue<>((a, b) -> {
            if (a.callTime == b.callTime)
                return a.score - b.score;
            return a.callTime - b.callTime;
        });

        // 실행 우선순위
        PriorityQueue<Program> runPQ = new PriorityQueue<>((a, b) -> {
            if (a.score == b.score)
                return a.callTime - b.callTime;
            return a.score - b.score;
        });

        for (int[] p : program) {
            callPQ.offer(new Program(p[0], p[1], p[2]));
        }

        long[] answer = new long[11];
        long currentTime = 0;

        while (!callPQ.isEmpty() || !runPQ.isEmpty()) {

            // 현재 시간까지 도착한 프로그램 이동
            while (!callPQ.isEmpty() && callPQ.peek().callTime <= currentTime) {
                runPQ.offer(callPQ.poll());
            }

            // 실행 가능한 프로그램이 없다면 시간 점프
            if (runPQ.isEmpty()) {
                currentTime = callPQ.peek().callTime;
                continue;
            }

            // 가장 우선순위 높은 프로그램 실행
            Program now = runPQ.poll();

            long wait = currentTime - now.callTime;
            answer[now.score] += wait;

            currentTime += now.runTime;
        }

        answer[0] = currentTime;
        return answer;
    }
}
