import java.util.*;

class Solution {
    public int solution(int[] menu, int[] order, int k) {
        int answer = 0;
        int finishTime = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        
        for (int i=0; i<order.length; i++) {
            int startTime = i * k;
            
            // 나가는 손님이 먼저 퇴장한 다음에 들어오는 손님이 입장해야하므로 손님퇴장
            while (!pq.isEmpty() && pq.peek() <= startTime) {
                pq.poll();
            }
            
            // 비어있으면 바로 음료제작
            // 앞사람있으면 앞사람 끝나는시간 + 내음료제작시간
            finishTime = pq.isEmpty() ? startTime + menu[order[i]] : Math.max(finishTime, pq.peek()) + menu[order[i]];
            
            pq.add(finishTime);
            answer = Math.max(answer, pq.size());
        }
        
        return answer;
    }
}
