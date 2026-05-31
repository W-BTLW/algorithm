from collections import deque

def solution(storage, requests):
    n = len(storage)        # 창고 세로 길이
    m = len(storage[0])     # 창고 가로 길이

    # 2차원 리스트 변환
    board = [list(row) for row in storage]

    # 상, 하, 좌, 우 이동 방향
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

    def remove_by_forklift(target):
        # 0 0 0 0 0
        # 0 A A A 0
        # 0 A B A 0
        # 0 A A A 0
        # 0 0 0 0 0
        # 밖과 연결되어있는지 확인하기 위해 전체 기준으로 테두리를 생성
        # 실제 창고 좌표는 1 ~ n, 1 ~ m 위치에 있다고 가정
        
        # 방문여부 체크 배열생성
        visited = [[False] * (m + 2) for _ in range(n + 2)]

        q = deque()

        # 창고 바깥 시작점인 (0, 0)
        q.append((0, 0))
        visited[0][0] = True

        remove_list = []

        while q:
            x, y = q.popleft()

            # 현재 좌표 기준으로 상, 하, 좌, 우를 하나씩 확인 && 다음으로 이동할 좌표 계산
            for dx, dy in directions:
                nx = x + dx
                ny = y + dy

                # 전체 위치기준으로 범위 오버하는 케이스
                if nx < 0 or nx >= n + 2 or ny < 0 or ny >= m + 2:
                    continue

                # 기 방문 위치 체크
                if visited[nx][ny]:
                    continue

                # 외부 감싼 영역이 아닌 내부영역인지 체크
                # 시작점 기준으로 한칸 테두리가 있기때문에 -1로 위치 처리
                if 1 <= nx <= n and 1 <= ny <= m:
                    real_x = nx - 1
                    real_y = ny - 1

                    # 해당 위치가 빈 공간이면 외부와 연결된 공간이므로 계속 탐색
                    if board[real_x][real_y] == "":
                        # 빈 공간을 방문 처리
                        visited[nx][ny] = True

                        # 빈 공간은 지나갈 수 있으므로 큐에 추가
                        q.append((nx, ny))

                    # 해당 위치가 요청한 컨테이너라면 제거 대상
                    elif board[real_x][real_y] == target:
                        # 해당 컨테이너 위치를 방문 처리
                        visited[nx][ny] = True

                        # 실제 board 기준 좌표를 제거 목록에 추가
                        remove_list.append((real_x, real_y))

                    # 빈 공간도 아니고 목표 컨테이너도 아니면 지나갈 수 없음
                    else:
                        continue

                # 다음 좌표가 창고 바깥 영역인 경우
                else:
                    visited[nx][ny] = True

                    # 창고 바깥 영역은 자유롭게 이동 가능하므로 큐에 추가
                    q.append((nx, ny))

        # 제거 대상 컨테이너를 빈 문자열로 바꿔서 제거 처리
        for x, y in remove_list:
            board[x][y] = ""

    def remove_by_crane(target):
        for i in range(n):
            for j in range(m):
                if board[i][j] == target:
                    board[i][j] = ""

    for request in requests:
        target = request[0]

        # 요청 길이가 1이면 지게차 요청
        if len(request) == 1:
            # 외부와 연결된 컨테이너만 제거
            remove_by_forklift(target)

        # 요청 길이가 2이면 크레인 요청
        else:
            # 해당 컨테이너를 전부 제거
            remove_by_crane(target)

    answer = 0

    for i in range(n):
        for j in range(m):
            if board[i][j] != "":
                answer += 1
                
    return answer
