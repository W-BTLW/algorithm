// 손님의 도착시간-퇴장시간을 구해서 얼마나 겹치는가 
/* 예시1
menu = [5, 12, 30]
order = [1, 2, 0, 1]
k = 10

0초  C0입장  {C0}
10초 C1입장  {C0, C1}
12초 C0퇴장  {C1}
20초 C2입장  {C1, C2}
30초 C3입장  {C1, C2, C3}
42초 C1퇴장  {C2, C3}
47초 C2퇴장  {C3}
59초 C3퇴장  {}
*/

class Solution {
    public int solution(int[] menu, int[] order, int k) {
        int answer = 0;

        int n = order.length;  // 손님 수
        int prevFinish = 0;  // 앞 손님 퇴장시간
        Queue<Integer> queue = new LinkedList<>();  // 매장 안 (선입선출)

        for (int i=0;i<n;i++){
            int arrival = i*k;  // 손님 도착 시각

            // 매장 안이 비어있지 않고, 도착시간보다 이 전에 퇴장했으면 큐에서 제거
            while(!queue.isEmpty() && (queue.peek() <= arrival)) {
                queue.poll();
            }

            // 음료 만들기 시작하는 시각 = max(앞 손님 퇴장시각, 현 손님 도착시각)
            int start = Math.max(prevFinish, arrival);
            // 퇴장하는 시각 = 음료 만들기 시작하는 시각 + 제조시간 
            int finish = start + menu[order[i]];

            prevFinish = finish;
            queue.offer(finish);  // 큐에 퇴장하는 시각 저장

            answer = Math.max(answer, queue.size());  // 매장 안에 있는 사람 최댓값

        }


        return answer;
    }
}
