package com.vuvarov.rashod.repository.specification;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class OperationSpecification implements Specification<Operation> {
    private final OperationFilterDto filter;

    @Override
    public Predicate toPredicate(Root<Operation> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getAccountId() != null) {
            Join<Object, Object> accountToTransferJoin = root.join("accountToTransfer", JoinType.LEFT);
            predicates.add(
                    builder.or(
                            builder.equal(root.get("account").get("id"), filter.getAccountId()),
                            builder.equal(accountToTransferJoin.get("id"), filter.getAccountId())
                    ));

        }
        if (StringUtils.isNotBlank(filter.getTag())) {
            predicates.add(builder.like(root.get("tags"), "%" + filter.getTag() + "%"));
        }

        if (StringUtils.isNotBlank(filter.getPlace())) {
            predicates.add(builder.like(root.get("place"), "%" + filter.getPlace() + "%"));
        }

        if (!CollectionUtils.isEmpty(filter.getCategoryIds())) {
            predicates.add(root.get("category").get("id").in(filter.getCategoryIds()));
        }
        if (!CollectionUtils.isEmpty(filter.getOperationTypes())) {
            predicates.add(root.get("operationType").in(filter.getOperationTypes()));
        }

        if (!CollectionUtils.isEmpty(filter.getAccountTypes())) {
            Join<Object, Object> accountToTransferJoin = root.join("accountToTransfer", JoinType.LEFT);
            predicates.add(builder.or(
                    root.get("account").get("accounType").in(filter.getAccountTypes()),
                    accountToTransferJoin.get("accounType").in(filter.getAccountTypes())
            ));
        }
        if (filter.getDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("operationDate"), filter.getDateFrom().atStartOfDay()));
        }

        if (filter.getDateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("operationDate"), filter.getDateTo().atStartOfDay()));
        }

        if (filter.getCurrency() != null) {
            Join<Object, Object> accountToTransferJoin = root.join("accountToTransfer", JoinType.LEFT);
            Predicate accountCurrency = builder.equal(root.get("account").get("currency"), filter.getCurrency());
            Predicate accountToTransferCurrency = builder.equal(accountToTransferJoin.get("currency"), filter.getCurrency());
            predicates.add(builder.or(accountCurrency, accountToTransferCurrency));
        }

        boolean isPlan = BooleanUtils.isTrue(filter.getIsPlan());
        predicates.add(builder.equal(root.get("plan"), isPlan));

        if (filter.getCostFrom() != null || filter.getCostTo() != null) {
            Predicate costFromPredicate = null;
            Predicate costToPredicate = null;
            Predicate currencyCostToPredicate = null;
            Predicate currencyCostFromPredicate = null;
            if (filter.getCostFrom() != null) {
                costFromPredicate = builder.greaterThanOrEqualTo(root.get("cost"), filter.getCostFrom());
                currencyCostFromPredicate = builder.greaterThanOrEqualTo(root.get("currencyCost"), filter.getCostFrom());
            }
            if (filter.getCostTo() != null) {
                costToPredicate = builder.lessThanOrEqualTo(root.get("cost"), filter.getCostTo());
                currencyCostToPredicate = builder.lessThanOrEqualTo(root.get("currencyCost"), filter.getCostTo());
            }

            predicates.add(
                    builder.or(
                            and(Stream.of(costFromPredicate, costToPredicate)
                                    .filter(Objects::nonNull).toArray(Predicate[]::new), builder),
                            and(Stream.of(currencyCostFromPredicate, currencyCostToPredicate)
                                    .filter(Objects::nonNull).toArray(Predicate[]::new), builder)
                    )

            );
        }


        if (predicates.size() == 0) {
            return null;
        }

        return predicates.size() > 1
                ? builder.and(predicates.toArray(new Predicate[0]))
                : predicates.get(0);
    }

    private Predicate and(Predicate[] predicates, CriteriaBuilder builder) {
        if (predicates.length == 1) {
            return predicates[0];
        }
        return builder.and(predicates);
    }
}
