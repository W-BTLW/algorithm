import java.math.*;
import java.util.*;

class Solution {

    public int solution(int[] bandage, int health, int[][] attacks) {
        int answer = 0;

        int lastAttacks = 0;

        for (int i=0;i<attacks.length;i++){
            lastAttacks = Math.max(lastAttacks, attacks[i][0]);  // 공격 마지막 번호 추출
        }

        int curHealth = health;
        int curTime=0;
        int curCombo=0;

        Map<Integer, Integer> monster = new HashMap<>();
        for(int i=0;i<attacks.length;i++){
            monster.put(attacks[i][0], attacks[i][1]);
        }

        while(curHealth > 0 && curTime != lastAttacks){
            curTime++;  // 시간

            if(monster.containsKey(curTime)){  // 몬스터 공격이 있으면
              curCombo = 0;  // 콤보 초기화
              int damage = monster.get(curTime);

              curHealth -= damage;

              if (curHealth <= 0) break;  // 체력 소진하면 바로 종료

            } else {  // 몬스터 공격이 없으면
              // 체력 증진하장
              curHealth += bandage[1];
              curCombo += 1;
              
              // 체력증진 한 값이 max를 넘거나 이미 max일 때
                if (curHealth >= health){
                    curHealth = health;
                }
              // combo 찼을 때
                else if (curCombo == bandage[0]){
                    curHealth += bandage[2];
                    if(curHealth >= health){
                        curHealth = health;
                    }
                    curCombo=0;  //콤보 다시 초기화
                }

            }
        }

        answer = curHealth<=0 ? -1 : curHealth;


        return answer;
    }    
}