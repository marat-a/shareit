package ru.practicum.server.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest , Long> {

    List<ItemRequest> findItemRequestsByRequestor_IdOrderByCreated (long requestorId);

    Page<ItemRequest> findAllByRequestor_IdNot (long userId , Pageable pageable);
}
