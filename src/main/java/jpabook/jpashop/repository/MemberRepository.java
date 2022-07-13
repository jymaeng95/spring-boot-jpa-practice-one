package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // Spring Data JPA가 타입을 보고 쿼리 생성
    // select m from Member m where m.name = ? -> JPQL을 생성
    List<Member> findByName(String name);
}
