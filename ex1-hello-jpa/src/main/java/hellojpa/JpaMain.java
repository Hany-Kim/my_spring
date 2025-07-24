package hellojpa;

import jakarta.persistence.*;
import java.util.List;
import jdk.swing.interop.LightweightFrameWrapper;

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
            Movie movie = new Movie();
            movie.setDirector("aaaa");
            movie.setActor("bbbb");
            movie.setName("바람과함께사라지다");
            movie.setPrice(10000);

            /*
            * insert 쿼리가 Item과 Movie에 날아가는 것을 확인할 수 있음
            *  insert
            into
                Item (name, price, id)
            values
                (?, ?, default)
            *
            *  insert 
            into
                Movie (actor, director, id) 
            values
                (?, ?, ?)
            * */

            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMovie = " + findMovie);

            /*
            * select
                m1_0.id,
                m1_1.name,
                m1_1.price,
                m1_0.actor,
                m1_0.director
            from
                Movie m1_0
            join
                Item m1_1
                    on m1_0.id=m1_1.id
            where
                m1_0.id=?
            * */

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
