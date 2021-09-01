package com.neueda.atm.repository;

import com.neueda.atm.entity.AccountEntity;
import com.neueda.atm.entity.Bank;
import com.neueda.atm.entity.BankNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface BankNoteRepository  extends PagingAndSortingRepository<BankNote, Integer>, JpaSpecificationExecutor<BankNote> {
    @Override
    Page<BankNote> findAll(Pageable pageable);

    @Override
    Optional<BankNote> findById(Integer integer);

    @Override
    Optional<BankNote> findOne(Specification<BankNote> specification);
}
