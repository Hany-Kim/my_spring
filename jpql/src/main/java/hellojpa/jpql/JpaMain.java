package hellojpa.jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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
            /*
            * 프로젝션
            * - SELECT절에 조회할 대상을 지정하는 것
            * - 프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본데이터 타입
            * - SELECT m FROM Member m -> 엔티티 프로젝션
            * - SELECT m.team FROM Member m -> 엔티티 프로젝션
            * - SELECT m.address FROM Member m -> 임베디드 타입 프로젝션
            * - SELECT m.username, m.age FROM Member m -> 스칼라 타입 프로젝션
            * - DISTINCT로 중복 제거
            * */

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m left join Team t on m.username = t.name";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            /*
            * 조인 - ON절
            * - ON절을 활용한 조인(JPA 2.1부터 지원)
            *   - 1. 조인 대상 필터링
            *     - JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A';
            *     - SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t on m.TEAM_ID=t.id AND = 'A';
            *   - 2. 연관관계 없는 엔티티 외부 조인 (하이버네이트 5.1부터)
            *     - JPQL: SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name;
            *     - SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t on m.username = t.name;
            * */

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
