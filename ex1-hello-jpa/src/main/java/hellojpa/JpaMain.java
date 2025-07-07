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
            /*
            * 준영속 상태로 만드는 방법
            * - em.detach(entity) : 특정 엔티티만 준영속 상태로 전환
            * - em.clear() : 영속성 컨텍스트를 완전히 초기화
            * - em.close() : 영속성 컨텍스트를 종료
            * */

            // 영속
            Member member = em.find(Member.class, 150L); // -> 가져오면 영속상태가 된다.
            member.setName("AAAAA"); // 값이 변경되면 '더티 체킹'이 된다.

            em.detach(member); // 준영속상태로 전환 -> DB를 확인하면 데이터가 변경되지 않게 된다.
//            Hibernate:
//                select
//                    m1_0.id,
//                    m1_0.name
//                from
//                    Member m1_0
//                where
//                    m1_0.id=?
//            ===================
            /*
            * select 쿼리만 전송되고
            * update 쿼리는 생기지 않았음을 확인할 수 있다.
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
