package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable  // JPA의 내장 타입
@Getter
// 값 타입은 변경이 되면 안됌 (생성 시에만 값이 생성되게만 사용)
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // 자바 기본 생성자를 public, protected로 설정 - JPA 구현 라이브러리가 객체를 생성할 때 레플렉션 같은 기술을 사용하기 위해 기본 생성자 필요
    protected Address() {

    }

    // 생성자에서 값 초기화
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
