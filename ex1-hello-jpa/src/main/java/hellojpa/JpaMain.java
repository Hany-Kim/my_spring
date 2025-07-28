package hellojpa;

import jakarta.persistence.*;
import java.util.List;

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
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB= new Team();
            teamB.setName("teamA");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member2");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamB);
            em.persist(member2);

            em.flush();
            em.clear();

//            Member m = em.find(Member.class, member2.getId());

            List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();
            /*
            * fetch = FetchType.EAGER (즉시로딩)으로 설정되어 있는데,
            * Member객체를 조회하는 쿼리와 Team객체를 조회하는 쿼리가 따로 따로 2번 날아가는 것이 확인된다.
            * em.find()는 PK를 찍어서 가져오기 때문에 JPA 내부적으로 최적화할 수 있다.
            * 그런데, JPQL은 그대로 SQL로 번역하기 때문에 "select m from Member m"를 번역한 쿼리는 Member만 가져온다.
            * 그러면 Team객체는 조회하지 않았다. 그런데 즉시로딩으로 설정되어 있다....
            * 그러면 Member객체에 맞춰서 다시 Team객체를 조회해야 한다.
            * ==> N + 1 문제가 발생된다.
            *
            *   select
                    m1_0.MEMBER_ID,
                    m1_0.createdBy,
                    m1_0.createdDate,
                    m1_0.lastModifiedBy,
                    m1_0.lastModifiedDate,
                    t1_0.TEAM_ID,
                    t1_0.createdBy,
                    t1_0.createdDate,
                    t1_0.lastModifiedBy,
                    t1_0.lastModifiedDate,
                    t1_0.name,
                    m1_0.USERNAME
                from
                    Member m1_0
                left join
                    Team t1_0
                        on t1_0.TEAM_ID=m1_0.team_TEAM_ID
                where
                    m1_0.MEMBER_ID=?

                select
                    m1_0.MEMBER_ID,
                    m1_0.createdBy,
                    m1_0.createdDate,
                    m1_0.lastModifiedBy,
                    m1_0.lastModifiedDate,
                    m1_0.team_TEAM_ID,
                    m1_0.USERNAME
                from
                    Member m1_0

                 select
                    t1_0.TEAM_ID,
                    t1_0.createdBy,
                    t1_0.createdDate,
                    t1_0.lastModifiedBy,
                    t1_0.lastModifiedDate,
                    t1_0.name
                from
                    Team t1_0
                where
                    t1_0.TEAM_ID=?
            * */

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
