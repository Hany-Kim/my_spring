package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService {

    /*
    생성자 주입방식
    구현체를 변경하더라도 ~~~ServiceImple에서 변경은 생기지 않는다.
     */
    private final MemberRepository memberRepository;

    /*
    @Autowired는 MemberRepository타입에 맞는 Bean을 찾아서 자동으로 의존관계 주입을 해준다.
    MemoryMemberRepository에 @Component을 붙아준다.

    AutoAppConfig에는 AppConfig와 다르게 의존 관계를 주입해주는 부분이 작성되어 있지 않다.
    */
    @Autowired // ac.getBean(MemberRepository.class);
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
