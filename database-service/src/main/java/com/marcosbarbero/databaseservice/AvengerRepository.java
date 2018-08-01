package com.marcosbarbero.databaseservice;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvengerRepository extends PagingAndSortingRepository<Avenger, Integer> {
    List<Avenger> findAll();
}
