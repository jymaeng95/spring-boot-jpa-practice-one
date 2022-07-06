package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    // Init DB를 호출하기
    /**
     * @PostConstruct를 이용해 빈이 생성된 이후에 초기 DB 세팅호출
     */
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();

        // 상세코드를 여기에 바로 작성하면 스프링 라이프사이클로인해 트랜잭셔널이 먹지 않음
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("userA", "서울", "불정로", "111");
            em.persist(member);

            Book book = createBook("JPA1 Book", 100, 10000);
            em.persist(book);

            Book book2 = createBook("JPA2 Book", 100, 10000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "부산", "진주", "123");
            em.persist(member);

            Book book = createBook("Spring1 Book", 200, 20000);
            em.persist(book);

            Book book2 = createBook("Spring2 Book", 300, 40000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int stockQuantity, int price) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}


