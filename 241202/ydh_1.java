// PCCP 기출문제 1회 1번 붕대감기
class Solution {
    public int solution(int[] bandage, int health, int[][] attacks) {

        int h = health; // 초기 체력
        int t = 0; // 연속 성공
        int attacks_idx = 0;
        int end_t = attacks[attacks.length-1][0]; // 몬스터 어택의 마지막 공격이 마지막 시간임
        
        for(int i=1; i<=end_t; i++) {
            if (i == attacks[attacks_idx][0]) {
                // 몬스터 공격 시간임
                t=0; // 연속 성공 초기화
                h -= attacks[attacks_idx++][1]; // 어택이누
                if (h < 1) {
                    // game over
                    h = -1;
                    break;
                }
            } else {
                // 피 채울수 있는 시간임
                ++t; // 연속 성공 카운트한번
                if (t == bandage[0]) {
                    // 연속 피채우기 성공이네 ??
                    h += bandage[2];
                    t = 0;
                }
                h += bandage[1]; // 기본 피채우기

                if (h > health) h = health; // 최대치 못넘음 ㅠ
            }
        }

        return h;
    }
}
