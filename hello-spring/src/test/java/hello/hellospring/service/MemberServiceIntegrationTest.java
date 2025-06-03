package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
@SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다. (통합테스트)
즉, 스프링의 모든 의존성 주입, 빈 등록, 환경 설정 등을 실제와 같이 적용한 후 테스트를 진행
통합테스트보다 단위테스트(스프링 컨테이너를 띄우지 않고)를 잘 만드는게 좋은 테스트 방법이다.
------------
테스트 시 @Transactional을 사용하면,
테스트에 사용한 데이터로 DB에 변경사항이 있을 때, 이전 상태로 rollback한다.
즉, 테스트에 사용한 데이터를 DB에 반영하지 않는다.

@Transactional이 없다면 매번 @AfterEach를 활용해 지워줘야 한다.
 */
@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    /*
    테스트코드는 제일 끝단에 위치한 작업이다.
    가장 편한 방법을 사용하면 된다.
    생성자 주입방식보다는 @Autowired를 사용하면 편하다.
     */
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {// 테스트 코드는 외국인과 협업하는게 아니라면 '한글'로 적는 편도 있음
        // given 상황
        Member member = new Member();
        member.setName("spring");

        // when 케이스
        Long saveId = memberService.join(member);

        // then 결과
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        /*
        try {
            memberService.join(member2);
            fail(); // 테스트에 예외가 발생해야 함.
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
        */

        // then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}