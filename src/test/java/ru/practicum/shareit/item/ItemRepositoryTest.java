package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Найти вещи по названию или описанию")
    void getItemsByText() {
        User user1 = new User(null, "petr", "petr@mail.ru");
        em.persist(user1);
        Item item1 = makeItem("Магнит", "сувенир", true, user1);
        em.persist(item1);
        Item item2 = makeItem("Магнит2", "сувенир2", false, user1);
        em.persist(item2);
        Item item3 = makeItem("магнит3", "сувенир3", true, user1);
        em.persist(item3);
        Item item4 = makeItem("Открытка", "сувенир4", true, user1);
        em.persist(item4);
        Item item5 = makeItem("Сувенир", "магнит5", true, user1);
        em.persist(item5);
        PageRequest pr = PageRequest.of(0, 20);
        List<Item> items = itemRepository.findAllByNameOrDescriptionContainsIgnoreCase("Магни", pr);
        assertThat(items).hasSize(3).contains(item1, item3, item5);
    }

    private Item makeItem(String name,
                          String description,
                          Boolean available,
                          User user) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(user);
        return item;
    }
}
