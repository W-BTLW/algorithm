import heapq

def solution(program):
    # 1. 프로그램 목록을 호출 시간 오름차순으로 정렬
    program.sort(key=lambda x: x[1])
    
    # answer[0]은 전체 종료 시각, answer[1]~[10]은 각 우선순위별 대기 시간
    answer = [0] * 11
    
    hq = []      # 대기 큐 (우선순위 큐)
    now = 0      # 현재 시각
    idx = 0      # 대기 큐로 들어갈 다음 프로그램의 인덱스 포인터
    n = len(program)
    
    # 처리할 프로그램이 남아있거나 대기 큐에 프로그램이 있는 동안 반복
    while idx < n or hq:
        # 1. 현재 시각(now)까지 호출된 모든 프로그램을 대기 큐(hq)에 삽입
        while idx < n and program[idx][1] <= now:
            p, t, c = program[idx]
            heapq.heappush(hq, [p, t, c])
            idx += 1  # pop 대신 인덱스를 뒤로 한 칸 이동
            
        # 2. 대기 큐가 비어있다면, 다음 프로그램이 호출될 시간으로 점프
        if not hq:
            now = program[idx][1]
            continue
            
        # 3. 대기 큐에서 가장 우선순위가 높은 프로그램을 꺼내어 실행
        p, t, c = heapq.heappop(hq)
        
        # 대기 시간 누적
        answer[p] += (now - t)
        
        # 현재 시각 업데이트 (실행 시간만큼 경과)
        now += c
        
    # 모든 프로그램이 종료된 시각을 answer[0]에 기록
    answer[0] = now
    
    return answer
