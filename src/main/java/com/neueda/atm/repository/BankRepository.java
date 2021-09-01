package com.neueda.atm.repository;

import com.neueda.atm.entity.AccountEntity;
import com.neueda.atm.entity.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;
@Repository
public interface BankRepository extends PagingAndSortingRepository<Bank, Integer>, JpaSpecificationExecutor<Bank> {
    @Override
    Page<Bank> findAll(Pageable pageable);

    @Override
    Optional<Bank> findById(Integer integer);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000")})
    Bank findBankByNameEquals(String name);
}
