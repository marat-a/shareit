package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;
    @ManyToOne
    @JoinColumn (name="owner")
    private User owner;
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne
    @JoinColumn (name="item_request")
    private ItemRequest request;

    public Item(Long id, User owner, String name, String description, Boolean available) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
