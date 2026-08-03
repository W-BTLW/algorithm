def solution(menu, order, k):
    order_list = []
    answer = 0
    people = []
    for i in range(len(order)):
        order_list.append(menu[order[i]])
        people.append(len(order_list))
        # 첫음료주문 종료시간 - 다음손님 도착시간
        order_list[0] = order_list[0] - k
        # 다음손님이 도착했을때 음료가 여러개 나가있을수도 있으니 while문
        while order_list and order_list[0] <= 0:
            if len(order_list) >= 2:
                order_list[1] += order_list[0]
            order_list.pop(0)
    answer = max(people)
    return answer
