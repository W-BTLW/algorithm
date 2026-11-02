## 1. START WITH ... CONNECT BY (오라클 전용 방식)

**개념 설명**
오라클에서 전통적으로 사용하는 직관적인 계층형 쿼리입니다. `START WITH`로 최상위 노드를 정하고, `CONNECT BY`로 부모-자식 관계를 연결합니다. 오라클이 내부적으로 계층의 깊이(`LEVEL`)와 자식 유무(`CONNECT_BY_ISLEAF`)를 계산해 주어 코드가 매우 짧습니다.

**정답 쿼리 및 해설**

```sql
SELECT COUNT(ID) AS COUNT, GENERATION
FROM (
    SELECT 
        ID, 
        LEVEL AS GENERATION, 
        CONNECT_BY_ISLEAF AS IS_LEAF
    FROM ECOLI_DATA
    START WITH PARENT_ID IS NULL           -- 1세대(부모가 없는 데이터)부터 탐색 시작
    CONNECT BY PRIOR ID = PARENT_ID        -- 이전 단계의 ID가 현재 단계의 PARENT_ID인 데이터 연결
)
WHERE IS_LEAF = 1                          -- 자식이 없는 개체(멸종위기)만 필터링
GROUP BY GENERATION
ORDER BY GENERATION;

```

* **`LEVEL`**: 탐색 단계가 깊어질수록 1부터 자동으로 1씩 증가하므로, 서브쿼리 없이 바로 세대(GENERATION)를 구할 수 있습니다.
* **`CONNECT_BY_ISLEAF`**: 현재 개체의 자식이 없으면 1, 있으면 0을 반환합니다. 멸종위기 대장균을 즉시 판별하는 핵심 키워드입니다.

---

## 2. Recursive CTE (표준 SQL 방식)

**개념 설명**
`WITH` 절을 이용해 가상 테이블을 만들고, 자기 자신을 반복 참조(`UNION ALL`)하여 계층을 탐색하는 ANSI 표준 방식입니다. 오라클 11gR2 이상 및 MySQL, PostgreSQL 등 대부분의 DB에서 동일하게 작동합니다.

**정답 쿼리 및 해설**

```sql
WITH CTE AS (
    -- Anchor: 1세대 대장균 (초기 조건)
    SELECT ID, 1 AS GENERATION
    FROM ECOLI_DATA
    WHERE PARENT_ID IS NULL
    
    UNION ALL
    
    -- Recursive: 다음 세대 대장균 (반복 조건)
    SELECT E.ID, C.GENERATION + 1
    FROM ECOLI_DATA E
    JOIN CTE C ON E.PARENT_ID = C.ID
)
SELECT COUNT(C.ID) AS COUNT, C.GENERATION
FROM CTE C
WHERE C.ID NOT IN (
    -- 부모로 한 번이라도 등장한 ID 목록을 구해 제외 (자식이 없는 개체만 남김)
    SELECT PARENT_ID 
    FROM ECOLI_DATA 
    WHERE PARENT_ID IS NOT NULL
)
GROUP BY C.GENERATION
ORDER BY C.GENERATION;

```

* **세대 계산**: 초기 조건에서 1을 부여하고, 자식을 찾을 때마다 이전 세대(GENERATION)에 1을 더하는 방식으로 직접 세대를 추적합니다.
* **단말 노드 찾기**: `ISLEAF` 같은 자동 기능이 없으므로, 전체 테이블에서 누군가의 부모(`PARENT_ID`)로 등록된 적 없는 ID만 필터링(`NOT IN`)하여 자식이 없는 개체를 구합니다.

---

## 3. 최종 요약

| 비교 기준 | START WITH ... CONNECT BY | Recursive CTE (WITH 절) |
| --- | --- | --- |
| **호환성** | 오라클(Oracle) 전용 | 대부분의 관계형 DB 지원 (ANSI 표준) |
| **코드 간결성** | 매우 짧고 직관적 | 상대적으로 길고 서브쿼리 활용 필요 |
| **세대/깊이 확인** | `LEVEL` 가상 컬럼으로 즉시 확인 | `+ 1` 연산을 통해 누적하여 직접 계산 |
| **단말 노드 확인** | `CONNECT_BY_ISLEAF` (1 또는 0 반환) | `NOT IN` 또는 `LEFT JOIN`으로 직접 필터링 |
| **사용 전략** | 오라클 환경에서 빠른 쿼리 작성이 필요할 때 | 다른 DB로의 이관을 고려하거나 복잡한 확장이 필요할 때 |
