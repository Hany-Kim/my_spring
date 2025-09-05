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

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<Team> result = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();
            /*
            * select
                    t1_0.id,
                    t1_0.name
                from
                    Member m1_0
                join
                    Team t1_0
                        on t1_0.id=m1_0.TEAM_ID
            * */

            /*
            * 참고) 경로표현식
            * select m.team from Member m -> 실제로는 이렇게 사용하는 것을 지양
            * join이 사용될 때는, join쿼리도 한눈에 보여야 한다.
            * join은 성능에 영향을 많이주고, 쿼리 튜닝할 요소가 많기 때문.
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
