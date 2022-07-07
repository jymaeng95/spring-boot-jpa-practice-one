package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    /**
     * ManyToOne, OneToOne 성능 최적화
     * Order
     * Order -> Member
     * Order -> Delivery
     */

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember()       // 여기까진 프록시객체
                    .getName();     // Lazy 강제 초기화 후 DB에서 데이터 가져옴
            order.getDelivery()
                    .getAddress();
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        // Order SQL 1번 실행 -> 결과 주문수 2개
        // N + 1문제라고 한다!! 첫번째 쿼리의 결과로 N번의 쿼리가 추가 실행된다.
        // N + 1 : 주문 1 + 회원 N + 배송 N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // Member, Delivery 각 두번 씩 조회
        return orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());

        // 쿼리 5번이 나감 (너무나도 많이 나옴)
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        // Fetch Join 이용
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();     // LAZY가 초기화 : MemberId를 가지고 영속성 컨텍스트를 조회한 후 없는 경우 DB에 쿼리를 날린다.
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
