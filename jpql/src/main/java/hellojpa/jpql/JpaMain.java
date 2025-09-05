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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            /*
            * JPQL 문법
            * - 엔티티와 속성은 대소문자 구분 O (Member, username)
            * - JPQL키워드는 대소문자 구분 X (SELECT, FROM, WHERE, ...)
            * - 엔티티 이름 사용, 테이블 이름이 아님(Member)
            * - 별칭은 필수(m) (as 생략가능)
            * */
            /*
            * 집합과 정렬
            * - COUNT(), SUM(), AVG(), MAX(), MIN()
            * - select COUNT(m), SUM(m.age), AVG(m.age), MAX(m.age), MIN(m.age) from Member m
            * - GROUP BY, HAVING
            * - ORDER BY
            * */
            /*
            * - TypeQuery : 반환 타입이 명확할 때, 사용
            * - Query : 반환 타입이 명확하지 않을 때, 사용
            * */
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            query1.getResultList();

            Member result = query1.getSingleResult();
            System.out.println("result = " + result);

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
