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

            Member result = em.createQuery("select m from Member m where m.username = ?1", Member.class)
                    .setParameter(1, "member1")
                    .getSingleResult();

            /*
            * 위치기준 바인딩도 있지만 왠만하면 지향
            * */

            System.out.println("result = " + result.getUsername());

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
