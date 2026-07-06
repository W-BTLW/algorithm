import java.util.*;

class Solution {
    public long[] solution(int[][] programInput) {
        long[] answer = new long[11];
        int N = programInput.length;
        
        Program[] programs = new Program[N];
        for (int i = 0; i < N; i++) {
            programs[i] = new Program(programInput[i][0], programInput[i][1], programInput[i][2]);
        }
        
        // 호출시간 기준으로 programs배열 정렬
        Arrays.sort(programs, (a, b) -> Integer.compare(a.callTime, b.callTime));
        
        // 우선순위 큐 대기열 준비
        PriorityQueue<Program> ready = new PriorityQueue<>((a, b) -> {
            if (a.priority != b.priority) {  //우선순위 낮은 순
                return Integer.compare(a.priority, b.priority);
            }
            return Integer.compare(a.callTime, b.callTime);  // 우선순위 동일하면 호출시간 빠른 순
        });
        
        long curTime = 0;  //현재시간
        int index = 0;  //index번째 프로그램
        
        while (index < N || !ready.isEmpty()) { // 대기열 빌 때 까지 반복
            
            while (index < N && programs[index].callTime <= curTime) {  // 현재시간기준, 시작했어야 하거나 시작해야하는 program 대기열에 추가
                ready.add(programs[index]);
                index++;
            }
            
            if (ready.isEmpty() && index < N) {  // 대기열에 프로그램 없음 && 아직 실행해야 하는 프로그램 있음
                curTime = programs[index].callTime;  // 다음 프로그램의 호출시간으로 넘어감
                continue;
            }
            
            Program curJob = ready.poll();  // 대기열에서 빼서 실행
            
            long waitTime = curTime - curJob.callTime;  // 대기시간 = 현재 시간 - 대기실 도착 시간
            answer[curJob.priority] += waitTime;  // 대기시간 기록
            
            curTime += curJob.execTime;  // 실행시간 만큼 현재시간 점프
        }
        
        // 최종 종료 시각 저장
        answer[0] = curTime;
        
        return answer;
    }

    
    static class Program{
        int priority;
        int callTime;
        int execTime;

        public Program(int priority, int callTime, int execTime){
            this.priority = priority;
            this.callTime = callTime;
            this.execTime = execTime;
        }
    }
}
