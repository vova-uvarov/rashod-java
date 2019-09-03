package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.enums.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE LOWER(a.name) LIKE LOWER(?1)")
    Page<Account> findAllByName(String q, Pageable page);

    List<Account> findAllByIsBalanceTrueAndAccountType(AccountType accountType);
}
