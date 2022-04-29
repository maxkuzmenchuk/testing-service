package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsRepository extends JpaRepository<OptionEntity, Long> {
}
