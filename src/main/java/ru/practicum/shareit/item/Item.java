package yandex.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import yandex.practicum.shareit.request.ItemRequest;
import yandex.practicum.shareit.user.User;

@Data
@AllArgsConstructor
public class Item {
    Long id;
    Long owner;
    String name;
    String description;
    Boolean available;
    ItemRequest request;
}
