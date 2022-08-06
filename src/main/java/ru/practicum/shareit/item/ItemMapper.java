package yandex.practicum.shareit.item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest()
        );
    }

    public static Item toItem(ItemDto itemDto, Long owner) {
        return new Item(
                itemDto.getId(),
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequest()
        );
    }

    public static List<ItemDto> toItemDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
