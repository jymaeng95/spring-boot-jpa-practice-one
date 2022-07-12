package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();  // 데이터 예제 2개

        // 컬렉션 타임은 아직 못넣었기 때문에 한개씩 넣어준다.
        // 각 주문 아이디당 컬렉션 조회한 데이터 넣어주기
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    // 일대다 관계의 데이터를 한번에 넣얼 수 없기 때문에 (N개의 row가 뻥튀기) 새로운 JPQL을 작성
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select" +
                " new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +  // 주문상품(OrderItem), 상품(Item)은 toOne 관계이므로 조인을 해도 데이터가 늘어나지 않는다.
                " join oi.item i" +
                " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    // 1:1 관계인 데이터는 한번에 조회해서 DTO에 넣어준다.
    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select" +
                " new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    // V5 컬렉션 최적화
    public List<OrderQueryDto> findAllByDtoOptimization() {
        List<OrderQueryDto> result = findOrders();

        // 조회한 주문ID를 모두 뽑아온다. (2개 조회)
        List<Long> orderIds = toOrderIds(result);

        // 루프를 돌지 않고 데이터를 한번에 가져온다. (쿼리를 1번 날림)
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        // 결과를 매핑
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("select" +
                " new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +  // 주문상품(OrderItem), 상품(Item)은 toOne 관계이므로 조인을 해도 데이터가 늘어나지 않는다.
                " join oi.item i" +
                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // orderItems를 Map으로 변경해준다. (메모리에 세팅)
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream().map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }

    // V6
    public List<OrderFlatDto> findAllByDtoFlat() {
        return em.createQuery("select" +
                " new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
