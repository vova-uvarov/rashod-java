package com.vuvarov.rashod.web;

import com.vuvarov.rashod.configuration.Dictionaries;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final IOperationService operationService;
    private final Dictionaries dictionaries;

    @GetMapping("/all")
    Dictionaries dictionaries() {
        return dictionaries;
    }

    @GetMapping("/tags")
    List<String> tags() {
        return operationService.getAllTags();
    }

    @GetMapping("/years")
    List<Integer> years() {
        return IntStream.rangeClosed(operationService.minOperationDate().getYear(), LocalDate.now().getYear())
                .boxed()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
}
