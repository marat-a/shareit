package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

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
