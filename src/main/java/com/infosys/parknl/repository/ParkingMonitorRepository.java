package com.infosys.parknl.repository;

import com.infosys.parknl.model.ParkingMonitorDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingMonitorRepository extends MongoRepository<ParkingMonitorDAO, Long> {
}
