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
            // 비영속 상태 (new / transient)
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            // 영속 상태 (managed)
            System.out.println("BEFORE"); // DB에 저장된다면 SQL이 날아감. SQL이 출력되는 시점을 확인
            em.persist(member); // 엄밀히 말해서 DB에 저장하는 것은 아님
            System.out.println("AFTER");
//                BEFORE
//                AFTER
//                Hibernate:
//                   /* insert for
//                        hellojpa.Member */insert
//                                    into
//                            Member (name, id)
//                            values
//                                    (?, ?)

            // 준영속 상태 (detached)
//            em.detach(member); // 영속성 컨텍스트에서 분리

            // 삭제 상태 (removed)
//            em.remove(member); // DB 삭제 요청

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
