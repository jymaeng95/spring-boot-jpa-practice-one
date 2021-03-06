package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
// 기본 생성자를 막아줘야한다!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private int orderPrice;

    private int count;

    //== 생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 주문 들어온 만큼 재고를 줄여준다/
        item.removeStock(count);

        return orderItem;
    }

    //== 비즈니스 로직 ==//
    public void cancel() {
        //재고를 주문 수량만큼 다시 복구해준다.
        getItem().addStock(count);
    }

    //== 조회 로직 ==/
    /**
     * 주문한 상품의 가격 * 개수
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
