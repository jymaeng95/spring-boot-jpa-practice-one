package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 상품_저장_최초() {
        // given
        Item item = new Book();
        item.setName("다이어리");

        // when
        Long savedId = itemService.saveItemOne(item);

        // then
        assertEquals(item, itemRepository.findOne(savedId));
    }

    @Test
    public void 상품_저장_있는경우() {
        // given
        Item item = new Book();
        item.setName("다이어리");
        Long savedId = itemService.saveItemOne(item);

        // when
        item.setName("새로운 다이어리");
        Long updatedId = itemService.saveItemOne(item);

        // then
        assertEquals(savedId, updatedId);
        assertEquals("새로운 다이어리", itemRepository.findOne(savedId).getName());
    }

    @Test
    public void 상품_조회() {
        // given
        Item item = new Book();
        item.setName("다이어리");
        Long savedId = itemService.saveItemOne(item);

        // then
        Item findItem = itemRepository.findOne(savedId);

        // then
        assertEquals(findItem.getId(), savedId);
        assertEquals(findItem.getName(), "다이어리");
    }

    @Test
    public void 상품_리스트_조회() {
        // given
        Item item1 = new Book();
        item1.setName("다이어리");

        Item item2 = new Book();
        item2.setName("다이어리 2");

        itemService.saveItem(item1);
        itemService.saveItem(item2);

        // when
        List<Item> findItems = itemRepository.findAll();

        // then
        assertEquals(findItems.size(), 2);
    }
}