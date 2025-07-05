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
            Member findMember1 = em.find(Member.class, 101L); // 1번째 조회에서 DB에서 데이터를 가져오고
            Member findMember2 = em.find(Member.class, 101L); // 2번째는 1차 캐싱에서 데이터를 가져와야 함.

//            Hibernate:
//                select
//                    m1_0.id,
//                    m1_0.name
//                from
//                    Member m1_0
//                where
//                    m1_0.id=?
            /*
            * select 쿼리가 1번만 실행된것을 확인할 수 있다.
            * */

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
