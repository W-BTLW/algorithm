class Solution {
    public String[] solution(int[][] queries) {
        int N = queries.length;
        String[] answer = new String[N];
        
        for (int i = 0; i < N; i++) {
            int n = queries[i][0];
            int p = queries[i][1];
            answer[i] = find(n, p);
        }
        return answer;
    }

/*
    1
    1 2 3 4
    1234 5678 910112 13141516
    ...

    부모가 RR/rr이면 나도 RR/rr
    부모가 Rr이면 내가 부모의 몇 번째 자식인지 확인

    나의 parent는 ((p-1)/4)+1 번째
    나는 parent의 ((p-1)%4)+1 번째 자식
    */
    private String find(int n, int p) {
        if (n == 1) return "Rr";

        int parent = ((p-1)/4)+1;   
        int child = ((p-1)%4)+1;

        String parentEl = find(n - 1, parent);  //재귀

        if (parentEl.equals("RR")) return "RR";  //부모가 RR이면 자식도 RR
        if (parentEl.equals("rr")) return "rr";  //부모가 rr이면 자식도 rr

        // 부모가 Rr이면
        if (child == 1) return "RR";
        if (child == 4) return "rr";
        return "Rr";
    }
}
