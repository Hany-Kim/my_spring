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
            Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");

//            em.persist(member);
            /*
            * em.persist(member); 수정 후 DB에 저장하는 로직은 필요하지 않다.
            *
            * JPA의 목적은 JAVA Collection처럼 사용하는 것이다.
            * Collection를 사용할 때도 값을 변경하고 다시 저장하지 않는다.
            * 그렇기에 오히려 em.persist()를 사용하면 안된다.
             */
//            Hibernate:
//                select
//                    m1_0.id,
//                    m1_0.name
//                from
//                    Member m1_0
//                where
//                    m1_0.id=?
//            ==================
//                    Hibernate:
//            /* update
//                for hellojpa.Member */update Member
//                set
//                    name=?
//                where
//                    id=?
            /*
            em.persist()없이 update 쿼리가 날아간것을 확인할 수 있다.
             */

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
