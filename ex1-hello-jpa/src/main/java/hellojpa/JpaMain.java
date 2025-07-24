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

//            Movie findMovie = em.find(Movie.class, movie.getId());
//            System.out.println("findMovie = " + findMovie);

            Item item = em.find(Item.class, movie.getId());
            System.out.println("item = " + item);
            /*
            * 객체 지향이니 movie타입이 아닌 상위 타입인 item타입으로도 조회할 수 있어야 한다.
            *
            * JPA는 조회할 때 union all로 다 찾아온다. => 쿼리가 복잡해진다.
            * DTYPE이 없다.
            *
            * select
                i1_0.id,
                i1_0.clazz_,
                i1_0.name,
                i1_0.price,
                i1_0.artist,
                i1_0.author,
                i1_0.isbn,
                i1_0.actor,
                i1_0.director
            from
                (select
                    price,
                    id,
                    artist,
                    name,
                    null as author,
                    null as isbn,
                    null as actor,
                    null as director,
                    1 as clazz_
                from
                    Album
                union
                all select
                    price,
                    id,
                    null as artist,
                    name,
                    author,
                    isbn,
                    null as actor,
                    null as director,
                    2 as clazz_
                from
                    Book
                union
                all select
                    price,
                    id,
                    null as artist,
                    name,
                    null as author,
                    null as isbn,
                    actor,
                    director,
                    3 as clazz_
                from
                    Movie
            ) i1_0
        where
            i1_0.id=?
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
