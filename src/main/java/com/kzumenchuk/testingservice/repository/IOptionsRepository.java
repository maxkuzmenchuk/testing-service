package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOptionsRepository extends JpaRepository<OptionEntity, Long> {
}
