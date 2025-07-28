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
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            /*
            // 문제
            // parent만 관리하고 싶은데 .persist()할때, child를 함께 .persist()해야하는 번거로움이 있다.
            em.persist(parent);
            em.persist(child1);
            em.persist(child2);
            */

            /*
            // 이때 부모를 .persist()하면 자식을 알아서 .persist()하고 싶다. 이때 Casecade를 사용한다.
            em.persist(parent);
            // 앞선 상황과 동일하게 parent, child1, child2에 대한 .persist()가 수행된것을 insert문이 날아간것으로 확인할 수 있다.
            */

            em.persist(parent);
            em.persist(child1);
            em.persist(child2);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
//            findParent.getChildList().remove(0); // DELETE 쿼리 날아감
            /*
            * orphanRemoval = true 에 의해서 연관관계가 끊어진 자식 엔티티 0번째 인덱스가 지워짐
            *
            * delete
                from
                    Child
                where
                    id=?
            * */

            em.remove(findParent); // parent뿐만 아니라 child1, child2를 모두 제거되는 DELETE쿼리가 3번 날아간것을 알 수 있음
            /*
            * Hibernate:
            delete
            from
                Child
            where
                id=?

            Hibernate:
            delete
            from
                Child
            where
                id=?

            Hibernate:
            delete
            from
                Parent
            where
                id=?
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
