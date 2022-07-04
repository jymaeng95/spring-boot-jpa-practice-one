package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    // 레포지토리에 위임하는 서비스
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Long saveItemOne(Item item) {
        return itemRepository.saveOne(item);
    }

    @Transactional // Transactional통해서 commit이 되고 flush가 호출됨 -> 영속성 엔티티에서 바뀐 부분을 업데이트 쿼리를 날림
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        // ID 기반으로 영속상태 엔티티 찾아옴
        Item findItem = itemRepository.findOne(itemId);

        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
