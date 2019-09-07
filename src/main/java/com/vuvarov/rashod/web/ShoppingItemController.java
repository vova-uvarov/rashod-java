package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.ShoppingItem;
import com.vuvarov.rashod.repository.ShoppingItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Pattern;
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
    public List<String> all() {
        List<String> allNames = repository.findAllNames();
        return allNames.stream()
                .filter(StringUtils::isNoneBlank)
                .flatMap(n -> Pattern.compile(",").splitAsStream(n))
                .filter(StringUtils::isNoneBlank)
                .map(StringUtils::trim)
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }
}
