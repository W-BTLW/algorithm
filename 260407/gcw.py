def solution(queries):
    answer = []
    for n,p in queries:
        answer.append(find_shape(n, p))
    
    return answer

# [EX]find_shape(3,9) / cur_gen : 0 
#      -> find_shape(2,3) / cur_gen : 2 
#      -> find_shape(1,1) -> Rr(Root) 
#      -> Rr / cur_gen : 2 ==> Rr
#      -> Rr / cur_gen : 0 ==> RR
      
def find_shape(n, p):
    # 최상위 root 부분 return
    if n == 1:
        return "Rr"

    # 현세대 기반 상위세대 위치 찾기
    prev_gen = (p - 1) // 4 + 1
    cur_gen = (p - 1) % 4

    # root 만날때까지 계속 재귀로 호출
    prev_shape = find_shape(n - 1, prev_gen)

    # RR / rr은 무조건 동일하게 return 처리
    if prev_shape == "RR":
        return "RR"
    if prev_shape == "rr":
        return "rr"

    # 상위 세대 Rr일 때
    # 첫번째 -> RR / 네번째 -> rr / 나머지 -> Rr
    if cur_gen == 0:
        return "RR"
    elif cur_gen == 3:
        return "rr"
    else:
        return "Rr"
