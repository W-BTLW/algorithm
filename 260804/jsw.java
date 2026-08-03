import java.util.*;

class Solution {
    public int solution(int[] menu, int[] order, int k) {
        int answer = 0;
        int time = 1000001;
        int left = 0;
        int index = 0;
        int person = 0;
        
        Queue<Integer> q = new LinkedList<>();
        
        for(int t=0; t<time; t++){
            // k초마다 손님이 도착, 마지막 손님까지만 받음
            if((t % k == 0) && (index < order.length)) {
                   q.offer(menu[order[index++]]);
            }
            
            // 제조 중인 음료가 없다면 제조 시작
            if(left == 0){ 
                if(!q.isEmpty()) {
                    left = q.poll();
                    person = 1;
                } else {
                    person = 0;
                    continue;
                }
            }
            
            left--;
            
            answer = Math.max(answer, q.size() + person);
        }
        
        return answer;
    }
}
