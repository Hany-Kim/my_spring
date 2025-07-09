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
            // 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team); // 연관관계의 주인은 team이다.
            em.persist(member);

            team.getMembers().add(member); // 어차피 읽기 전용이라 JPA에서 업데이트할 때 사용하지 않는다.
            /*
            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            Team team = new Team();
            team.setName("TeamA");
            team.getMembers().add(member); // mappedBy라서 읽기 전용이다. 역방향(주인이 아닌 방향)만 연관관계가 설정됨.
            em.persist(team);

            * DB를 확인하면 MEMBER테이블의 TEAM_ID가 null이다.
            * 현재 연관관계의 주인은 Member클래스의 team이다.
            * team.getMembers().add(member); // mappedBy라서 읽기 전용이다.
            * JPA에서 insert, update할때는 .add하지 않는다.
            * */

            /*
            * Member와 Team은 영속상태이기에 1차 캐시에서 조회하기 때문에
            * 별도의 Select 쿼리가 날아가지 않는다.
            * Select 쿼리를 확인하기 위해서는
            * em.flush();
            * em.clear();
            * 를 추가하면된다. */
            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();

            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
