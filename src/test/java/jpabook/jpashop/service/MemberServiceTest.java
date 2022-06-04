package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)  // Spring과 함께 사용한다는 것.
@SpringBootTest  // Spring Boot를 띄운 상태로 테스트를 실행한다는 것
@Transactional // 기본적으로 롤백을함 (테스트 케이스에서 사용되는 경우에만)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;


    @Test
    @Rollback(value = false)
    public void 회원가입() {
        // given - 주어진 조건
        Member member = new Member();
        member.setName("kim");

        // when - 이걸 실행할 때
        // 해당 서비스는 persist를 실행 -> insert문이 안나감 = commit 되는 시점에 insert문이 나감
        Long savedId = memberService.join(member);

        // then - 결과
        // 같은 트랜잭션 내에서 동일 영속성 컨텍스트를 사용 시
        em.flush();  // 영속성 컨텍스트에 있는 것을 DB로 반영하기 때문에 Rollback을 설정 안해도 쿼리확인 가능
        assertEquals(member, memberRepository.findMember(savedId));

    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);

       /* try {
            memberService.join(member2);  // 예외가 발생해야함
        } catch(IllegalStateException e) {
            return;
        }*/

        // then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // Assert.fail() 해당 부분에 코드가 오면 안되는데 오는 경우는 잘못된 테스트이다. 잘못된 테스트 코드임을 확인시켜주는 메서드
//        fail("예외가 발생해야 합니다");
    }
}