package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor  // final 필드만 가지고 생성자를 만듬
@Transactional(readOnly = true) // JPA는 트랜잭션 내에서 데이터 변경이 되므로 반드시 설정 (클래스 레벨)
// 조회에서 성능 최적화 -> 영속성 컨텍스트 더티 체킹 안함, 데이터베이스에 따라 읽기 전용 트랜잭션으로 처리하므로 리소스 읽기 모드로 사용(드라이버)
public class MemberService {
    // final로 설정하면
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional // 클래스 레벨보다 우선권을 가짐
    public Long join(Member member)  {
        // 중복회원 검증
        validateDuplicateMember(member);

        memberRepository.save(member);

        //DB PK와 매핑 (영속성 컨텍스트에 영속화 상태가 되기 때문)
        return member.getId();
    }

    // WAS를 여러개 쓰고있기 때문에 validate 로직을 두개 동시에 호출 하는 경우가 있음. -> 실무에서는 MultiThread상황을 고려해서 DB의 유니크 제약조건을 거는게 좋음
    private void validateDuplicateMember(Member member) {
        // 중복회원인 경우 예외 발생
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findMember(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findMember(id);
        member.setName(name);
    }
}
