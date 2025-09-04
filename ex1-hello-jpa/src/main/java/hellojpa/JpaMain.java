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
            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            // flush -> commit, query 날아갈 때 동작
            // 아직 DB에 반영안되었다.

            // JPA관련된 기술을 사용할 때는 큰 문제 없다. 단, DB커넥션을 얻어올 때 주의
            // dbconn.executeQuery("select * from member"); -> JPA랑 관련없다. = flush 안된다. = DB로 쿼리가 안갔다. => 결과 0 (아무것도 없다)
            // 이런 경우 직전에 flush 해야한다.

            List<Member> resultList = em.createNativeQuery("select MEMBER_ID, city, street, zipcode, USERNAME from MEMBER",
                            Member.class)
                    .getResultList(); // -> flush 되었다.

            for (Member member1 : resultList) {
                System.out.println("result = " + member1);
            }

            /*
            * JDBC 직접 사용, SpringJdbcTemplate
            * - JPA를 사용하면서 JDBC커넥션을 직접 사용하거나, SpringJdbcTemplate, 마이바티스등을 함께 사용 가능
            * - 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시 필요
            * - 예) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 플러시
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
