package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
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

    //== 비즈니스 로직 ==//

    /**
     * stock 재고 증가
     * @param quantity 증가 시킬 재고 양
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 재고 감소
     * @param quantity 감소 시킬 재고 양
     */
    // setter가 아닌 핵심 비즈니스 메서드를 통해 이용하는 것이 좋음 - 객체 지향적인 코드
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
