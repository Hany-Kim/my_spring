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
            member1.setUsername("hello1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("hello2");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m2 = em.getReference(Member.class, member1.getId());
            Member m3 = em.getReference(Member.class, member1.getId());
            System.out.println("m2: " + m2.getClass());
            System.out.println("m3: " + m3.getClass());
            System.out.println("m2 == m3: " + (m2.getClass() == m3.getClass()));
            /*
            * m2: class hellojpa.Member$HibernateProxy$0heI9Bbe
            * m3: class hellojpa.Member$HibernateProxy$0heI9Bbe
            *
            * m2 == m3: true 같은 프록시 객체가 비교된다.
            * */

            // --------------------------------

            Member m1 = em.find(Member.class, member1.getId()); // 영속성 상태에 올라감
            System.out.println("m1 == " + m1.getClass());

            Member reference = em.getReference(Member.class, member1.getId());
            System.out.println("reference = " + reference.getClass());

            /*
            * JPA에서는 같은 트랜잭션 내에서 비교연산자 == 사용시 같다라고 나와야한다.
            * */

            /*
            * m1 == class hellojpa.Member
            * reference = class hellojpa.Member
            *
            * reference가 프록시 객체가 아닌 원본(Member)객체가 출력되는 것을 알수 있다.
            *
            * 이미 Member를 영속성컨텍스트 올려놨는데(1차캐시) 굳이 프록시 객체를 반환하는 것보다
            * 원본 객체를 반환하는 것이 성능 최적화면에서 좋다.
            *
            * 또한 JPA에서는 System.out.println("a == a: " + (m1 == reference));는 항상 true가 나와야한다.
            * JPA에서는 m1이 프록시든 원본이든 상관없이 ==비교가 한 영속성컨텍스트에서 가져온 것이고 PK가 똑같으면 항상 true를 반환해야한다.
            * */
            System.out.println("a == a: " + (m1 == reference));

            // --------------------------------

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass());

            Member findMember = em.find(Member.class, member1.getId()); // 영속성 상태에 올라감
            System.out.println("findMember = " + findMember.getClass());

            System.out.println("refMember == findMember: " + (refMember.getClass() == findMember.getClass()));
            /*
             * refMember = class hellojpa.Member$HibernateProxy$ZWiCpflg
             * findMember = class hellojpa.Member$HibernateProxy$ZWiCpflg
             * refMember == findMember: true
             *
             *  refMember와 findMember가 모두 프록시 객체가 나온다.
             *
             * proxy가 한번 조회되면 em.find에서 proxy객체를 그냥 반환한다.
             * 그래야 JPA == 비교 연산자를 맞출 수 있다.
             *
             * 그렇기에 프록시가 나오든 실제 엔티티가 나오든 문제없도록 개발해야 한다.
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
