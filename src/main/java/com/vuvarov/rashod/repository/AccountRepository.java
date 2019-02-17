package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository  extends CrudRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE LOWER(a.name) LIKE LOWER(?1)")
    List<Account> findAllByName(String q);
}
