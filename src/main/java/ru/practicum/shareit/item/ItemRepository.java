package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item AS i WHERE i.available = TRUE " +
            "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(i.description) " +
            "LIKE LOWER(CONCAT('%', :text, '%')))")

    List<Item> findAllByNameOrDescriptionContainsIgnoreCase(@Param("text") String text);
    List<Item> findItemsByOwnerIdOrderById(Long userId);

}