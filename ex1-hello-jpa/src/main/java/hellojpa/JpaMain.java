package hellojpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
            // Criteria 사용 준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> resultList = em.createQuery(cq).getResultList();

            /*
            * Criteria
            * - JPQL에서 쿼리는 코드 상에서 단순 문자열이다. ("select m from Member m where m.username like '%kim%'")
            *   따라서, 동적쿼리를 만드는 것이 어렵다. (동적쿼리 쉽게 -> Criteria, MyBatis, IBatis, ...)
            *   동적 쿼리가 없다면 문자열을 이어붙이고, 자르고 해야한다. => 오류가 많이 생길 수 있다.
            *
            * - 문자가 아닌 JAVA코드로 JPQL을 작성할 수 있다.
            * - JPQL 빌더 역할
            * - JPA 공식 기능
            * - 너무 복잡하고 실용성이 없다.
            *   가독성이 떨어지는 것이 단점. -> 복잡한 쿼리 작성하는게 어렵다.
            * - QueryDSL 사용 권장
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
