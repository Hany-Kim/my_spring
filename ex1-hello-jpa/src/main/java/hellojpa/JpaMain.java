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
            /*
            * JPA 관점에서는 연관관계의 주인의 값만 설정해도 된다.
            * Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);
            * */

            // 객체지향 관점에서는 연관관계의 주인뿐만 아니라 역방향도 값을 설정해줘야 한다.
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
//            member.changeTeam(team); // member.setTeam(team); Member기준으로 Team을 넣을때
            em.persist(member);

            team.addMember(member); // Team기준으로 Member를 넣을 때

            /*
            * tip)
            * member.setTeam(team);
            * team.getMembers().add(member); // 추가
            *
            * 항상 양방향 값을 설정하다보면 사람이기에 실수할 수 있음.
            * '연관관계 편의 메서드'를 작성해 실수를 줄이자.
            *
            * new Member()를 세팅하는 시점에 team.getMembers().add(member)를 반영
            *
            * ----
            *
            * 반대 방향을 기준으로 연관관계 편의 메서드를 작성할 수도 있다.
            * team.addMember(member);
            * 단, 이전에 작성한 연관관계 편의 메서드를 없애줘야 한다.
            * 최악의 경우 무한루프에 걸릴 수 있다.
            * 둘 중 하나만 선택하자.
            * */
            /*
            * team.getMembers().add(member); // 추가
            * 컬렉션 값을 설정하지 않으면, == 역방향 관계를 설정하지 않으면
            * 영속성 컨텍스트를 flush(), clear()할때는 이후 Select 쿼리가 추가로 날아가
            * Team에 속한 Member (Team -> Member) 방향으로 조회가 가능하지만
            *
            * flust(), clear()를 하지 않으면, 이후 Select 쿼리가 날아가지 않고
            * Insert 쿼리가 날아갈때 1차캐시에 있던 값으로 조회하기 때문에
            * Team에 속한 Member (Team -> Member) 방향으로 조회가 불가능
            *
            * 항상 양방향으로 값을 설정해야 한다. -> 중요
            * */

            /*
            * Member와 Team은 영속상태이기에 1차 캐시에서 조회하기 때문에
            * 별도의 Select 쿼리가 날아가지 않는다.
            * Select 쿼리를 확인하기 위해서는
            * em.flush();
            * em.clear();
            * 를 추가하면된다. */
            /*em.flush();
            em.clear();*/

            Team findTeam = em.find(Team.class, team.getId()); // 1차 캐시에 들어가 있다.
            List<Member> members = findTeam.getMembers();

            System.out.println("=============================");
//            System.out.println("members = " + findTeam); // Exception in thread "main" java.lang.StackOverflowError
            /*
            * member도 .toString()을 호출하고
            * team도 .toString()을 호출했다.
            *
            * member에 있던 team도 .toString()을 쓰고
            * team에 있던 member도 .toString()을 쓴다.
            *
            * 무한루프에 걸렸다.
            *
            * lombok에서 toString()을 왠만하면 사용하지 않아야 한다.
            * JSON 생성 라이브러리를 사용할 때는 Entity를 Dto로 변환해서 사용해야 한다. -> 대부분의 문제가 해결된다.
            * Entity만 사용하면 Entity스펙이 변경될 때, API 스펙이 변경된다.
            * */
            System.out.println("=============================");

            tx.commit(); // SQL 쿼리가 DB에 날아가는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
