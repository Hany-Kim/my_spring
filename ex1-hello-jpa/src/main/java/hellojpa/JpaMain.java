package hellojpa;

import jakarta.persistence.*;

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
            Address address = new Address("city", "street", "10000");

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(address);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(address); // member1에 저장한 주소와 같은 주소를 참조하고 있다.
            em.persist(member2);

            member.getHomeAddress().setCity("newCity"); // member1의 주소만 변경한다면?
            /*
            * Update 쿼리가 2번 날아간다. -> 수정한건 member 1개인데?
            * DB를 확인하면 member1, member2 두개의 주소가 update 되었다.
            * 같은 값을 참조하고 참조하는 값을 변경했기 때문
            * 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함.
            *
            * 의도해서 사용한다면 임베디드 값 타입이 아닌 엔티티로 만들어야 한다.
            *
            * Hibernate:
            update Member
            set
                city=?,
                street=?,
                zipcode=?,
                USERNAME=?,
                endDate=?,
                startDate=?
            where
                MEMBER_ID=?
            Hibernate:
            update Member
            set
                city=?,
                street=?,
                zipcode=?,
                USERNAME=?,
                endDate=?,
                startDate=?
            where
                MEMBER_ID=?
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
