def solution(queries):
    answer = []
    
    for n, k in queries:
        # 부모의 k가 맨위로 오게 (k -> 1,2,3,4)
        stack = []
        while n > 1:
            n -= 1
            stack.append((k-1) % 4 + 1)
            k = (k-1) // 4 + 1

        isAdd = False
        while stack:
            parentK = stack.pop()
            # 부모의 k가 1(RR)이거나 4(rr)이면 그 자식들은 무조건 부모랑 같음
            if parentK == 1:
                answer.append("RR")
                isAdd = True
                break
            if parentK == 4:
                answer.append("rr")
                isAdd = True
                break
        if not isAdd:
            answer.append("Rr")
    
    return answer
