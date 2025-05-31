package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository{
    //private static Map<Long, Member> store = new ConcurrentHashMap<>(); // 실무에선 동시성 문제 때문에 ConcurrentHashMap를 사용
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L; // 실무에선 동시성 문제로 Atomic Long 사용

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() // loop 돌림
                .filter(member -> member.getName().equals(name))
                .findAny(); // 하나라도 찾으면
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
