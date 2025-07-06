package hellojpa;

import jakarta.persistence.*;
import java.util.List;
import jdk.swing.interop.LightweightFrameWrapper;

public class JpaMain {

    public static void main(String[] args) {

        // 애플리케이션 로딩 시점에 딱 한개만 만들어 둔다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션 단위로 EntityManager를 생성하고 소멸시킨다.
        EntityManager em = emf.createEntityManager(); // DB 커넥션 하나를 받은것과 유사
        // EntityManager는 쓰레드간 절대 공유하면 안된다.

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 영속
            Member member = new Member(200L, "member200");
            em.persist(member);

            em.flush(); // .commit() 이전에 미리 DB에 반영하고 싶을 때
            /*
            * 플러시의 동작순서
            * 1. 더티 체킹 (변경 감지)
            * 2. 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
            * 3. 쓰기 지연 SQL 저장소의 쿼리를 DB에 전송
            * */

//            Hibernate:
//                /* insert for
//                    hellojpa.Member */insert
//                into
//                    Member (name, id)
//                values
//                    (?, ?)
//            ==================
            /*
            .commit() 이전에 SQL쿼리가 날아간 것을 확인할 수 있다.
             */

            /*
            플러시 하는 방법
            1. em.flush() 직접호출
            2. tx.commit() 트랜잭션 커밋 - 플러시 자동 호출
            3. JPQL 쿼리 실행 - 플러시 자동 호출
                em.persit(memberA);
                em.persit(memberB);
                em.persit(memberC);

                // 중간에 JPQL 실행
                query = em.createQuery("select m from Member m", Member.class);
                // JPQL 사용시 자동으로 flush가 호출되지 않으면
                // em.createQuery()의 결과를 가져올 수 없다. -> 아무 데이터도 없기 때문에
                // 그래서 JPQL 실행시점에 flush가 되어 이전에 작업한 데이터를 반영하고
                // JPQL 쿼리가 실행되어 결과적으로 Data를 가져올 수 있게된다.
             */

            /*
            플러시 모드 옵션
            em.setFlushMode(FlushModeType.COMMIT);
            - FlushModeType.AUTO : 커밋이나 쿼리를 실행할 때 플러시 (기본값)
            - FlushModeType.COMMIT : 커밋할 때만 플러시
             */

            /*
            * 플러시
            * - 영속성 컨텍스트를 비우지 않음
            * - 영속성 컨텍스트의 변경 내용을 DB에 동기화
            * - 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화
            * */

            System.out.println("==================");
            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
