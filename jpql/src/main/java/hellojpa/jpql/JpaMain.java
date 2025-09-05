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

            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            /*
            * select m from Member m 쿼리가 날아갔을 때,
            * select절에 있는 m은 Member 엔티티이다.
            * 반환타입도 Member엔티티들이다.
            *
            * 여기서, 반환타입의 List<Member>는 영속성 컨텍스트에 관리가 될것인가? 안 될것인가?
            * 영속성 컨텍스트에 관리된다. 그래서 엔티티 프로젝션이다.
            * */

            Member findMember = result.get(0);
            findMember.setAge(20); // DB업데이트 되면 영속성관리됨.


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
