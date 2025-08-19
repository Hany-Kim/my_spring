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

            member.getAddressHistory().add(new AddressEntity("old1", "street1", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street2", "20000"));

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

            // homeCity -> newCity
             // findMember.getHomeAddress().setCity("newCity"); // 사이드 이펙트 발생할 수 있다. 값타입은 immutable 해야한다.
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode())); // 값 타입은 항상 새로 넣어야 한다.

            // 치킨 -> 한식 변경
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            findMember.getAddressHistory().remove(new Address("old1", "street1", "10000")); // 대부분의 컬렉션들은 .equals로 값을 찾는다.
            /*
            해시코드가 잘 작성되지 않으면, 값이 지워지지 않고 무한히 쌓인다.
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Address address = (Address) o;
                return Objects.equals(city, address.city) &&
                        Objects.equals(street, address.street) &&
                        Objects.equals(zipcode, address.zipcode);
            }

            @Override
            public int hashCode() {
                return Objects.hash(city, street, zipcode);
            }
             */
//            findMember.getAddressHistory().add(new Address("newCity1", "street1", "10000"));
            /*
            * - 값 타입은 엔티티와 다르게 식별자 개념이 없다.
            * - 값은 변경하면 추적이 어렵다.
            * - 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고,
            *   값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
            * - 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야 함:
            *   null 입력x, 중복 저장x
            * */
            /*
             * - 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려하는 것이 좋다.
             * - 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입 사용
             * - 영속성전이 (cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션처럼 사용
             *
             * 값 타입 컬렉션은 진짜 단순할 때 사용, 값이 바뀌어도 업데이트하지 않을때
             * ex) 체크 버튼(치킨, 피자)기능
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
