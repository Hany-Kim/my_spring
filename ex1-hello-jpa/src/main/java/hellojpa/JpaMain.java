package hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team);
            em.persist(member1);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member1.getId());
            /*
            * Member객체만 조회하는 쿼리가 DB에 날아간것을 확인할 수 있음
            * select
                m1_0.MEMBER_ID,
                m1_0.createdBy,
                m1_0.createdDate,
                m1_0.lastModifiedBy,
                m1_0.lastModifiedDate,
                m1_0.TEAM_ID,
                m1_0.USERNAME
            from
                Member m1_0
            where
                m1_0.MEMBER_ID=?
            * */

            System.out.println("m = " + m.getTeam().getClass()); // m = class hellojpa.Team$HibernateProxy$OtB6T72W
            // Team객체는 프록시객체가 반환됨.

            System.out.println("====================");
            m.getTeam().getName();
            System.out.println("====================");
            /*
            * 프록시 객체를 초기화하는 시점(= 프록시 객체를 조회하는 시점(m.getTeam().getName()))에
            * Team객체를 조회하는 쿼리가 DB에 전달됨.
            * ====================
                Hibernate:
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
                ====================
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
