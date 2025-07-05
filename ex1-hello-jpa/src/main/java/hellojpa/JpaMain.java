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
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            em.persist(member1);
            em.persist(member2);
            System.out.println("===================");
            ===================
//            Hibernate:
//                /* insert for
//                    hellojpa.Member */insert
//                                into
//                        Member (name, id)
//                        values
//                                (?, ?)
//                        Hibernate:
//                /* insert for
//                    hellojpa.Member */insert
//                                into
//                        Member (name, id)
//                        values
//                                (?, ?)
            /*
            * member1, member2 두 객체가 영속성컨텍스트에 먼저 저장된뒤
            * SQL 쿼리 두개가 한번에 날아간다. => 버퍼링 기능 : 최적화가 가능하다
            * JDBC 배치
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
