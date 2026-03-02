// 테스트2에서 에러나는 코드임;;
class Solution {
    private static int answer = Integer.MAX_VALUE;
    private static int [][] map;
    private static int [] dr = {-1,1,0,0};
    private static int [] dc = {0,0,-1,1};
    private static int r_size, c_size; // row/col size
    private static int r_red_goal, c_red_goal; // red goal row/col
    private static int r_blue_goal, c_blue_goal; // blue goal row/col
    private static boolean[][] visitRed;
    private static boolean[][] visitBlue;

    public int solution(int[][] maze) {
        r_size = maze.length;
        c_size = maze[0].length;

        map = new int[r_size][c_size];
        visitRed = new boolean[rs][cs];
        visitBlue = new boolean[rs][cs];

        int r_red, c_red;
        int r_blue, c_blue;

        for(int i=0; i<r_size; i++) {
            for(int j=0; j<c_size; j++) {
                if(maze[i][j] == 0) continue;
                
                if(maze[i][j] == 1) {
                    // red 시작칸
                    r_red = i;
                    c_red = j;
                    visitRed[i][j] = true;
                }
                if(maze[i][j] == 2) {
                    // blue 시작칸
                    r_blue = i;
                    c_blue = j;
                    visitBlue[i][j] = true;
                }
                if(maze[i][j] == 3) {
                    // red goal
                    r_red_goal = i;
                    c_red_goal = j;
                }
                if(maze[i][j] == 4) {
                    // blue goal
                    r_blue_goal = i;
                    c_blue_goal = j;
                }
                if(maze[i][j] == 5) {
                    // 백트래킹 map에 벽 추가
                    map[i][j] = maze[i][j];
                }
            }
        }

        cartRolling(r_red, c_red, r_blue, c_blue, 0, false, false);
        return answer;
    }

    private static boolean validation(int r, int c){ // ArrayOutOfBoundsIndex 예외 방지 
        return 0 <= r && 0 <= c && r < r_size && c < c_size;
    }

    private static void cartRolling(int r_red, int c_red, int r_blue, int c_blue, int cnt, boolean r_end, boolean b_end) {
        // 종료지점 도착시 도착완료 표시
        if(!r_end && r_red == r_red_goal && c_red == c_red_goal) r_end = true;
        if(!b_end && r_blue == r_blue_goal && c_blue == c_blue_goal) b_end = true;

        if(r_end && b_end) {
            // 둘다 종료지점 도착
            answer = Math.min(answer, cnt);
            return;
        }

        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                int nr_red = r_red + dr[i];
                int nc_red = c_red + dc[i];
                int nr_blue = r_blue + dr[j];
                int nc_blue = c_blue + dc[j];

                // 맵 벗어날 때
                if(!validation(nr_red, nc_red) || !validation(nr_blue, nc_blue)) continue;
                // 벽 확인
                if(map[nr_red][nc_red] == 5 || map[nr_blue][nc_blue] == 5) continue;
                // 방문여부확인
                if( (!r_end && visitRed[nr_red][nc_red]) || (!b_end && visitBlue[nr_blue][nc_blue])) continue;
                // 서로 같은곳 안됨
                if(nr_red == nr_blue && nc_red == nc_blue) continue;
                // 서로 위치바꾸기 안됨
                if(nr_red == r_blue && nc_red == c_blue && nr_blue == r_red && nc_blue == c_red) continue;

                visitRed[nr_red][nc_red] = true;
                visitBlue[nr_blue][nc_blue] = true;
                cartRolling(nr_red, nc_red, nr_blue, nc_blue, cnt+1, r_end, b_end);
                visitRed[nr_red][nc_red] = false;
                visitBlue[nr_blue][nc_blue] = false;
            }
        }
    }
}
