package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 구현체를 넣어 사용할 것이므로 추상 클래스로
// 상속관계는 전략을 설정이 필요
@DiscriminatorColumn(name = "dtype")  // dtype을 보고 어떤 자식 클래스인지 판단?
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "itme_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
