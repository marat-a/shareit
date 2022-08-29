package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    Long id;
    @NotBlank
    String text;
    @ManyToOne
    @JoinColumn (name="item_id")
    Item item;
    @OneToOne
    @JoinColumn (name="author_id")
    User author;
    LocalDateTime created;

}
