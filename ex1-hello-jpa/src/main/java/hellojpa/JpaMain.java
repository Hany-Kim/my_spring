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
            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            Team team = new Team();
            team.setName("teamA");

            team.getMembers().add(member);
            /*
            * 1:다 단방향시에는 insert쿼리 2번 + update 쿼리 1번 수행
            * member -> insert 쿼리
            * team -> insert 쿼리
            * team.getMembers().add(member) -> update 쿼리
            *
            * 다:1 단방향에서는 insert쿼리 2번으로 동일한 작업을 수행함
            *
            * 결론적으로 다:1관계가 성능과 유지관리 측면에서 효율적이다..
            * */

            em.persist(team);

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
