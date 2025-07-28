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
            /*
            * 프록시 기초
            * em.find() vs em.getReference()
            * em.find() : DB를 통해 "실제 엔티티" 객체 조회
            * em.getReference() : DB 조회를 미루는 가짜(프록시) 엔티티 객체 조회
            * */

            Member member = new Member();
            member.setUsername("hello");
            em.persist(member);

            em.flush();
            em.clear();

//            Member findMember = em.find(Member.class, member.getId());
            // em.find()를 사용하면 DB에 조회 쿼리가 날아간것을 알 수 있다.

            Member findMember = em.getReference(Member.class, member.getId());
            // em.getReference()를 사용하면 바로 DB에 조회 쿼리가 날아가지 않는 것을 알 수 있다.

            /*
            * 출력을 하는 시점에 DB에 조회쿼리가 날아가는 것을 알 수 있다.
            *
            * findMember.id = 1 -> 출력
                Hibernate:
                    select
                        ...
                    from
                        Member m1_0
                    left join
                        Team t1_0
                            on t1_0.TEAM_ID=m1_0.TEAM_ID
                    where
                        m1_0.MEMBER_ID=?
                findMember.username = hello -> 출력
            *
            * findMember.id는 DB에 조회쿼리가 "날아가기 전"에 출력된것을 확인할 수 있는데,
            * em.getReference()에 파라미터로 member.getId()를 넣어줬기때문에
            * DB를 거치지 않고 그대로 출력된 것이다.
            *
            * findMember.username를 조회하기 위해 DB에 조회쿼리가 날아간것을 알 수 있다.
            *
            * */

            System.out.println("before findMember = " + findMember.getClass());
            // findMember = class hellojpa.Member$HibernateProxy$1SDo0JsL
            // em.getReference()를 사용하면 프록시클래스 임을 알 수 있다.
            /*
            * 프록시 특징
            * - 실제 클래스를 상속받아서 만들어진다.
            * - 실제 클래스와 겉모양이 같다. (상속)
            * - 사용하는 입장에서 진짜, 가짜(프록시) 객체를 구분하지 않고 사용해도 된다.(이론상)
            * - 프록시 객체는 실제 객체의 참조를 보관
            * - 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메서드를 호출
            *
            * 중요)
            * - 프록시 객체는 처음 사용할 때 한번만 초기화한다.
            * - 프록시 객체를 초기화할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님!
            *   초기화되면 프록시 객체를 통해서 실제 엔티티에 접근이 가능한 것임
            * - 프록시 객체는 원본 엔티티를 상속받는다. 따라서 타입체크에 유의
            *   (원본 엔티티와 프록시 객체 비교시, ==은 비교 실패한다. instance of를 사용해야한다.)
            * */
            /*
            * 프록시 객체의 초기화
            * ```
            * Member findMember = em.getReference(Member.class, member.getId());
            * findMember.getUsername();
            * ```
            * 1. 프록시 객체로 .getUsername()을 요청보낸다.
            * 2. 타겟(진짜 엔티티)이 값이 있다면 그값을 출력한다.
            *   없다면 영속성 컨텍스트에게 프록시 객체가 초기화 요청을 보낸다.
            * 3. 영속성 컨텍스트에 값이 없다면 DB 조회를 한다.
            * 4. DB조회를 통해 실제 Entity를 생성한다.
            * */

            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.username = " + findMember.getUsername()); // 앞에서 프록시 객체가 .getUsername()을 요청보냈기 때문에 DB쿼리를 이미 보냈다. DB에 조회하지 않고, 타겟에 있는 값을 그대로 출력한다.
            System.out.println("after findMember = " + findMember.getClass());

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
