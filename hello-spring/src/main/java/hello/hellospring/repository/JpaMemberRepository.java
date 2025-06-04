package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    /*
    JPA는 EntityManager로 모든 것을 동작

    build.gradle에서 data-jpa의존성을 추가하면 자동으로 컨테이너에 EntityManager가 등록됨.
    그것을 주입받아서 사용하면 됨.
     */
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        /*
        PK으로 조회하는 것과 다르게, JPA Query Language를 사용해야 함
         */
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        /*
        테이블 대상이 아닌 객체를 대상으로 쿼리를 날림
        JPA가 SQL로 번역함.
         */
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
