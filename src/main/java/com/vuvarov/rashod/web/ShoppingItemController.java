package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.ShoppingItem;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.ShoppingItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shoppingItems")
@RequiredArgsConstructor
public class ShoppingItemController extends RestRepositoryController<ShoppingItem, Long, ShoppingItemRepository> {

    @GetMapping("/search")
    public Iterable<ShoppingItem> search(@RequestParam String q, @PageableDefault Pageable pageable) {
        return repository.findAllByName("%" + q + "%", pageable);
    }

    @GetMapping("/names")
    public Map<OperationType, List<String>> all() {
        return Arrays.stream(OperationType.values())
                .collect(Collectors.toMap(t -> t, t -> repository.findAllNamesByOperationType(t)));
    }
}
