오라클에서 트리 구조나 계층형 데이터(조직도, 카테고리, 댓글 등)를 다룰 때 사용하는 재귀 쿼리 방식은 크게 두 가지로 나뉩니다.

**1. 오라클 전통 방식 (START WITH ... CONNECT BY)**
가장 널리 사용되며 직관적이고 코드가 짧은 오라클 전용 문법입니다.

* **`START WITH`**: 계층 구조의 최상위 노드(시작 조건)를 지정합니다.
* **`CONNECT BY`**: 부모 노드와 자식 노드 간의 관계를 정의합니다. `PRIOR` 키워드를 부모 쪽에 붙여서 방향을 결정합니다.
* **`LEVEL`**: 오라클에서 자동으로 제공하는 가상 컬럼으로, 계층의 깊이(Root는 1)를 나타냅니다.

```sql
SELECT 사원번호, 사원명, 관리자번호, LEVEL
FROM 사원테이블
START WITH 관리자번호 IS NULL           -- 최상위 관리자부터 시작
CONNECT BY PRIOR 사원번호 = 관리자번호; -- 내 사원번호가 다음 데이터의 관리자번호가 됨

```

**2. 표준 SQL 방식 (Recursive CTE - 오라클 11gR2 이상)**
`WITH` 절을 사용하여 자기 자신을 반복 호출하는 ANSI 표준 문법입니다. 다른 DB(MySQL, PostgreSQL 등)와 호환성이 좋습니다.

* **Anchor 부분**: 재귀를 시작할 최상위 초기 데이터를 정의합니다.
* **Recursive 부분**: `UNION ALL` 아래에 위치하며, 생성된 가상 테이블(자기 자신)을 조인하여 반복 조건을 정의합니다.

```sql
WITH 재귀CTE AS (
    -- 1. Anchor (시작점)
    SELECT 1 AS NUM FROM DUAL
    UNION ALL
    -- 2. Recursive (반복 조건)
    SELECT NUM + 1 
    FROM 재귀CTE 
    WHERE NUM < 5
)
SELECT NUM FROM 재귀CTE; -- 결과: 1, 2, 3, 4, 5

```

**핵심 요약:**

* **빠르고 간단한 계층 검색:** `START WITH ... CONNECT BY`
* **타 DB 호환 및 복잡한 연산 필요 시:** `WITH` 재귀 절
