package com.ay.mortgage_server_app.db.repositories;

import com.ay.mortgage_server_app.db.entities.AvgInterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AvgInterestRateRepository extends JpaRepository<AvgInterestRate, Long>, JpaSpecificationExecutor<AvgInterestRate> {

}