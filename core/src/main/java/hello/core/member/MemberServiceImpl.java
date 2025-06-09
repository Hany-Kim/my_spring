package hello.core.member;

public class MemberServiceImpl implements MemberService {

    /*
    생성자 주입방식
    구현체를 변경하더라도 ~~~ServiceImple에서 변경은 생기지 않는다.
     */
    private final MemberRepository memberRepository;

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
