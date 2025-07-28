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
            Member member = em.find(Member.class, 1L);
//            printMemberAndTeam(member); // em.find(Member.class, 1L)에서 쿼리가 날아갈때 Member와 Team을 "한번에" 찾아오면 원하는 출력을 할 수 있다.
            printMember(member); // em.find(Member.class, 1L)에서 쿼리가 날아갈때 Member와 Team을 "한번에" 찾아오면 Team을 찾을 필요는 없기 때문에 손해다.
            /*
            * JPA에서는 프록시와 지연로딩을 통해 위와 같은 상황의 문제를 해결한다.*/

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {
        /*Member랑 Team을 출력하는 메서드*/
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
}
