package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    /*
    Spring Data JPA가 JpaRepository를 상속받은 인터페이스가 있으면 자동으로 빈에 등록함.
     */

    @Override
    Optional<Member> findByName(String name);
}
