package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest(){
        Book book = em.find(Book.class, 1L);

        //TX 트랜잭션 내에서 변경하고 커밋되면 JPA는 변경분에 대해서 update쿼리를 날림 => 더티 체킹 = 변경 감지
        book.setName("asdfasdf");

        //
    }
}
