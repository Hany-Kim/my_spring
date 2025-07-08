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
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeamId(team.getId()); // -> 객체지향스럽지 않다. member.setTeam(team)이라고 작성되는게 자연스럽다.
            em.persist(member);

            // Member의 팀 소속을 알고자 할때.
            Member findMember = em.find(Member.class, member.getId());
            Long findTeamId = findMember.getTeamId();
            Team findTeam = em.find(Team.class, findTeamId);
            // 객체지향스럽지 않다. 연관관계가 없기 때문에, DB에서 데이터를 계속 찾아와야한다.
            /*
            * 객체를 테이블에 맞추어 데이터 중심으로 모델을 설계하면 협력 관계를 만들 수 없다.
            * - 테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
            * - 객체는 참조를 사용해서 연관된 객체를 찾는다.
            * - 테이블과 객체 사이에는 이런 큰 간격이 있다.
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
