class Solution {
    public int solution(int[] bandage, int health, int[][] attacks) {
        int maxHealth = health;
        int castTime = bandage[0];
        int healPerSec = bandage[1];
        int extraHeal = bandage[2];

        int nowTime = 0;          // 현재 시간
        int attackIdx = 0;        // 공격 인덱스
        int comboCount = 0;       // 연속 회복 카운트

        while (attackIdx < attacks.length) {
            nowTime++;

            // 공격 처리
            if (attacks[attackIdx][0] == nowTime) {
                health -= attacks[attackIdx][1];

                attackIdx++;
                comboCount = 0;  // 공격 맞으면 연속 회복 초기화
                if (health <= 0) break;
            } 
            // 회복 처리
            else {
                health += healPerSec;
                comboCount++;

                if (comboCount == castTime) {
                    health += extraHeal;
                    comboCount = 0;
                }

                // 최대 체력 유지
                health = Math.min(health, maxHealth);
            }
        }

        return health <= 0? -1 : health;
    }
}
