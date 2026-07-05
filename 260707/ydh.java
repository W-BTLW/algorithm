import java.util.*;

class Solution {

    class Task implements Comparable<Task> {
        long score, callTime, runTime;
        
        public Task(long score, long callTime, long runTime) {
            this.score = score;
            this.callTime = callTime;
            this.runTime = runTime;
        }
        
        @Override
        public int compareTo(Task t) {
            // 우선순위 같다면 먼저 도착한 놈부터
            if (Long.compare(this.score, t.score) != 0) {
                return Long.compare(this.score, t.score);
            } else {
                return Long.compare(this.callTime, t.callTime);
            }
        }
    }
    
    public long[] solution(int[][] program) {
        long[] answer = new long[11];
        
        PriorityQueue<Task> tasks = new PriorityQueue<>();
        ArrayList<Task> arr = new ArrayList<>();
        
        // 값 셋팅
        for(int[] i : program) arr.add(new Task(i[0], i[1], i[2]));
        arr.sort(new Comparator<Task>() { // 도착 순서대로 정렬
            @Override
            public int compare(Task o1, Task o2) {
                return Long.compare(o1.callTime, o2.callTime);
            }
        });

        int idx = 0;
        long currentTime = 0;
        
        while(idx < arr.size() || !tasks.isEmpty()){ // idx가 끝까지 갔거나 우선순위 큐가 비어있을 때까지

            for(; idx < arr.size() ; idx++){
                if (arr.get(idx).callTime <= currentTime){ // 현재 시간까지 호출된 녀석 넣기
                    tasks.offer(arr.get(idx));
                } else break;
            }

            if(!tasks.isEmpty()){ // 만약 큐에 Node가 있다면
                Task temp = tasks.poll();
                // 현재 시간 - 프로그램 도착시간 -> 대기 시간
                answer[(int)temp.score] += currentTime - temp.callTime ;
                // 현재 시간 + 프로그램 실행시간
                currentTime += temp.runTime;
                answer[0] = currentTime;
            } else currentTime++;
            // 만약 큐가 비어있거나 프로그램이 전부 도착하지 않았을 때 
            // 현재 시간에 1 더합니다.
        }
        return answer;
    }
}
