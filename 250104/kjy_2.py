def solution(diffs, times, limit):
    start = 1
    end = max(diffs)

    def puzzleGame(diffs, times, limit, level):
        time = 0
        for i in range(len(diffs)):
            if diffs[i] <= level:
                time += times[i]
            else:
                time += ((times[i] + times[i-1]) * (diffs[i] - level) + times[i])
            
            if limit < time:
                return False
        return True

    while start < end :
        level = (start + end) // 2

        if puzzleGame(diffs, times, limit, level):
            end = level
        else:
            start = level + 1
            
    return end