/* WITH RECURSIVE
-- 재귀적으로 반복 조회할 임시 결과 
-- ECOLI_GEN은 실제 생성되는 테이블이 아닌 쿼리 내 임시 결과 이름이다.
*/
WITH RECURSIVE ECOLI_GEN AS (

    /*
     PARENT_ID IS NULL인 개체는 부모가 없으므로 1세대로 판단
     */

    SELECT
        ID,                  -- 현재 개체 ID
        PARENT_ID,           -- 부모 개체 ID
        1 AS GENERATION      -- 최초 개체이므로 1세대 부여
    FROM ECOLI_DATA
    WHERE PARENT_ID IS NULL

    /*
     * [1단계 조회 결과]
     *
     * ID | PARENT_ID | GENERATION
     * ---+-----------+-----------
     *  1 | NULL      | 1
     *  2 | NULL      | 1
     */

    -- UNION ALL
    -- 최초 조회 결과와 재귀 조회 결과를 합친다.
    --
    -- UNION과 달리 중복 제거 작업을 수행하지 않는다.
    -- 이 문제는 각 개체의 ID가 유일하고 부모가 하나이므로
    -- 중복 제거가 필요하지 않다.

    UNION ALL

    /*
     * [2단계 : 자식 개체를 찾아 다음 세대 생성]
     *
     * 재귀 실행부(Recursive Query)이다.
     *
     * ECOLI_DATA E : 전체 대장균 데이터
     * ECOLI_GEN G  : 이전 단계까지 계산된 세대 정보
     *
     * 부모 ID와 현재 개체 ID를 연결해
     * 자식 개체를 찾는다.
     */

    SELECT
        E.ID,                    -- 새롭게 찾은 자식 개체 ID
        E.PARENT_ID,             -- 해당 자식의 부모 ID
        G.GENERATION + 1         -- 부모 세대에 1을 더한다.
    FROM ECOLI_DATA E

    INNER JOIN ECOLI_GEN G
        ON E.PARENT_ID = G.ID

    /*
     * [INNER JOIN 동작 예시]
     *
     * 최초 ECOLI_GEN에는 1세대인 ID 1, 2가 존재한다.
     *
     * E.PARENT_ID = G.ID 조건을 통해
     * 부모가 1 또는 2인 자식 개체를 조회한다.
     *
     * ID 3 -> PARENT_ID 2 -> 부모가 1세대
     * ID 4 -> PARENT_ID 2 -> 부모가 1세대
     * ID 5 -> PARENT_ID 2 -> 부모가 1세대
     *
     * 따라서 다음 결과가 생성된다.
     *
     * ID | PARENT_ID | GENERATION
     * ---+-----------+-----------
     *  3 | 2         | 2
     *  4 | 2         | 2
     *  5 | 2         | 2
     *
     * 다음 반복에서는 방금 생성된 2세대 데이터를 기준으로
     * 자식 개체를 다시 조회한다.
     *
     * ID 4의 자식 -> ID 6, 7
     *
     * 결과:
     *  6 | 4         | 3
     *  7 | 4         | 3
     *
     * 다음 반복에서는 ID 6의 자식인 ID 8을 찾는다.
     *
     * 결과:
     *  8 | 6         | 4
     *
     * 더 이상 자식이 발견되지 않으면 재귀가 종료된다.
     */
)

/*
 * [3단계 : 재귀 CTE의 최종 결과]
 *
 * ID | PARENT_ID | GENERATION
 * ---+-----------+-----------
 *  1 | NULL      | 1
 *  2 | NULL      | 1
 *  3 | 2         | 2
 *  4 | 2         | 2
 *  5 | 2         | 2
 *  6 | 4         | 3
 *  7 | 4         | 3
 *  8 | 6         | 4
 *
 * 이제 각 개체가 몇 세대인지 계산되었으므로
 * 자식이 없는 개체만 필터링하면 된다.
 */

SELECT
    COUNT(*) AS COUNT,      -- 세대별 자식 없는 개체 수
    G.GENERATION            -- 세대 번호
FROM ECOLI_GEN G

/*
 * [4단계 : NOT EXISTS로 자식 없는 개체 조회]
 *
 * NOT EXISTS는 서브쿼리의 조회 결과가
 * 한 건도 없을 때 TRUE를 반환한다.
 *
 * 현재 개체 G.ID를 부모로 가진 데이터가 있는지 확인한다.
 *
 * 예시 1) G.ID = 2
 *
 * SELECT 1
 * FROM ECOLI_DATA E
 * WHERE E.PARENT_ID = 2
 *
 * 결과 : ID 3, 4, 5에 해당하는 행이 존재함.
 * 따라서 EXISTS = TRUE
 *       NOT EXISTS = FALSE
 *
 * -> ID 2는 자식이 있으므로 제외한다.
 *
 * 예시 2) G.ID = 3
 *
 * SELECT 1
 * FROM ECOLI_DATA E
 * WHERE E.PARENT_ID = 3
 *
 * 결과 : 조회되는 행이 없음.
 * 따라서 EXISTS = FALSE
 *       NOT EXISTS = TRUE
 *
 * -> ID 3은 자식이 없으므로 포함한다.
 *
 * SELECT 1의 숫자 1은 실제 조회 값으로 사용되지 않는다.
 * EXISTS는 행의 존재 여부만 판단한다.
 */

WHERE NOT EXISTS (
    SELECT 1
    FROM ECOLI_DATA E
    WHERE E.PARENT_ID = G.ID
)

/*
 * [5단계 : 자식 없는 개체만 남은 결과]
 *
 * ID | GENERATION
 * ---+-----------
 *  1 | 1
 *  3 | 2
 *  5 | 2
 *  7 | 3
 *  8 | 4
 *
 * GROUP BY로 같은 세대끼리 묶고
 * COUNT(*)로 개체 수를 계산한다.
 */

GROUP BY G.GENERATION

-- 세대를 1세대부터 오름차순 정렬한다.
ORDER BY G.GENERATION;

/*
 * [최종 결과]
 *
 * COUNT | GENERATION
 * ------+-----------
 *   1   | 1
 *   2   | 2
 *   1   | 3
 *   1   | 4
 */
