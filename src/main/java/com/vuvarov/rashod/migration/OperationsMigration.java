package com.vuvarov.rashod.migration;

import com.vuvarov.rashod.model.*;
import com.vuvarov.rashod.model.dto.migration.OperationDto;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.repository.CategoryRepository;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.repository.ShoppingItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationsMigration {

    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;
    private final ShoppingItemRepository shoppingItemRepository;

    @Value(value = "classpath:migration/operations.csv")
    private Resource operationsFile;
    private Map<String, Account> name2Account;
    private Map<String, Category> name2CategoryId;

    private final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void migrate() throws IOException {
        loadAccounts();
        loadCategories();

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(new InputStreamReader(operationsFile.getInputStream()));
        long recordsCount = 0;
        for (CSVRecord record : records) {
            recordsCount++;
            OperationDto dto = OperationDto.builder()
                    .id(Long.valueOf(record.get(0)))
                    .author(record.get(1))
                    .category(record.get(2))
                    .comment(record.get(3))
                    .cost(new BigDecimal(record.get(4)))
                    .creationDate(LocalDateTime.parse(record.get(5), DF))
                    .description(record.get(6))
                    .operationDate(LocalDateTime.parse(record.get(7), DF))
                    .place(record.get(8))
                    .operationType(record.get(9))
                    .accountName(record.get(10))
                    .accountToTransferName(record.get(11))
                    .isPlan(Boolean.parseBoolean(record.get(12)))
                    .lastUpdated(LocalDateTime.parse(record.get(13), DF))
                    .tags(record.get(14))
                    .parentId(parseParentId(record))
                    .currencyCost(extractCurrencyCost(record))
                    .build();
                toModel(dto);

            log.info("record: {} ", dto);
        }
        log.info("recods count: {}", recordsCount);
    }

    private void loadCategories() {
        name2CategoryId = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(Category::getName, Function.identity()));
    }

    private void loadAccounts() {
        name2Account = StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(Account::getName, Function.identity()));
    }

    private void toModel(OperationDto dto) {
        Operation result = new Operation();
        result.setComment(dto.getComment());
        Account account = name2Account.get(dto.getAccountName());
        if (account == null) {
            throw new IllegalStateException("account not found with name: " + dto.getAccountName());
        }
        result.setAccount(account);
        result.setAccountToTransfer(name2Account.get(dto.getAccountToTransferName()));
        Category category = name2CategoryId.get(dto.getCategory());
        if (category == null) {
            throw new IllegalStateException("category not found with name: " + dto.getCategory());
        }
        result.setCategory(category);
        result.setTags(dto.getTags());
        result.setCost(dto.getCost());
        result.setCurrencyCost(dto.getCurrencyCost());
        result.setPlan(dto.getIsPlan());
        result.setPlace(dto.getPlace());
        result.setAuthor(dto.getAuthor());
        result.setOperationDate(dto.getOperationDate());
        result.setOperationType(OperationType.valueOf(dto.getOperationType()));

        Operation savedOperation = operationRepository.save(result);
        String description = dto.getDescription();
        if (StringUtils.isNotBlank(description)) {
            String[] shoppingLIst = description.split(",");
            for (String item : shoppingLIst) {
                ShoppingItem shoppingItem = new ShoppingItem();
                shoppingItem.setName(item.trim());
                shoppingItem.setOperationId(savedOperation.getId());
                shoppingItemRepository.save(shoppingItem);
            }
        }
    }

    private BigDecimal extractCurrencyCost(CSVRecord record) {
        String value = record.get(16);
        if (StringUtils.isBlank(value)) return null;
        return new BigDecimal(value);
    }

    private Long parseParentId(CSVRecord record) {
        String value = record.get(15);
        if (StringUtils.isBlank(value)) return null;
        return Long.valueOf(value);
    }
}
