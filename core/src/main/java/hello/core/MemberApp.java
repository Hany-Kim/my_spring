package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {

    public static void main(String[] args) {
//        MemberService memberService = new MemberServiceImpl();

//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService(); // memberService 변수에는 MemberServiceImpl이 담김

        /*
        Spring을 사용하는 버전으로 수정

        Spring은 모든 것이 ApplicationContext(Spring 컨테이너)라는 것으로 시작한다.
        ApplicationContext이 @Bean에 등록된 객체들을 Spring컨테이너(Spring 빈)에서 관리해준다.

        AnnotationConfigApplicationContext 어노테이션 기반으로 설정된 객체를 관리
        파라미터로 구성정보를 등록한 클래스(AppConfig) 넘김
         */
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);// .getBean(꺼내올 메서드 이름, 클래스 타입)

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
}
