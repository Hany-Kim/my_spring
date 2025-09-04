package hellojpa;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

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
            List<Member> result = em.createQuery(
                    "select m from Member m where m.username like '%kim%'", // Member는 객체
                    Member.class
            ).getResultList();

            /*
            * JPQL
            * - 테이블이 아닌 객체를 대상으로 검색하는 객체지향 쿼리
            * - SQL을 추상화해서 특정 데이터베이스 SQL에 의존 X
            * - JPQL을 한마디로 정의하면 객체 지향 SQL
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
