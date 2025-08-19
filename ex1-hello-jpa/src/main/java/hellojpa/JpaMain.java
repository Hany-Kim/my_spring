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
            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new Address("old1", "street1", "10000"));
            member.getAddressHistory().add(new Address("old2", "street2", "20000"));

            em.persist(member);
            /*
            값 타입이기에 FavoriteFoods와 AddressHistory는 다른 테이블이지만,
            Memeber에 의존된다.

            값 타입은 별도의 업데이트가 필요없다.
             */
            em.flush();
            em.clear();

            System.out.println("==========START==========");
            Member findMember = em.find(Member.class, member.getId());
            /*
            Collection들은 "지연로딩" 된다는 것을 알 수 있다.
            ==========START==========
            Hibernate:
                select
                    m1_0.MEMBER_ID,
                    m1_0.city,
                    m1_0.street,
                    m1_0.zipcode,
                    m1_0.USERNAME
                from
                    Member m1_0
                where
                    m1_0.MEMBER_ID=?
             */

            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = " + address.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

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
