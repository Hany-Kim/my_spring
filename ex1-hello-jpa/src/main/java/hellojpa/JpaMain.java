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
            * - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티가 반환된다.
            * - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
            *   하이버네이트는 org.hibernate.LazyInitializationException 예외를 터뜨림
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

            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass()); // Proxy

            /*
            // case. 1
            // 조회 쿼리를 DB에 날려 refMember.getUsername()를 조회한다.
            refMember.getUsername();
             */

            /*
            // case. 2
            em.detach(refMember);

            refMember.getUsername();
            System.out.println("refMember = " + refMember.getClass()); // refMember = class hellojpa.Member$HibernateProxy$TarPAodo
            // 예외 발생 -> org.hibernate.LazyInitializationException
             */

            // case. 3
            em.clear(); // 영속성 컨텍스트를 꺼버림

            refMember.getUsername();
            System.out.println("refMember = " + refMember.getClass()); // refMember = class hellojpa.Member$HibernateProxy$TarPAodo
            // 예외 발생 -> org.hibernate.LazyInitializationException

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
