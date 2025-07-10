package jpabook.jpashop;

import jakarta.persistence.*;
import java.util.List;
import jdk.swing.interop.LightweightFrameWrapper;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*
            * 방법 1. 양방향
            * Order order = new Order();
            * order.addOrderItem(new OrderItem());
            * */

            // 방법 2. 단방향
            Order order = new Order();
            em.persist(order);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            em.persist(order);

            /*
            * 일단 단방향으로 구현한뒤
            * JPQL 작성시나 실무 중 중간에 조회결과가 필요한 때가 있다.
            * 이때, 양방향을 고려하라.
            * */

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

