package com.infosys.parknl.repository;

import com.infosys.parknl.model.ParkingDetailsDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingDetailsRepository extends MongoRepository<ParkingDetailsDAO, Long> {
    List<ParkingDetailsDAO> findByLicensePlateNumber(String licensePlateNumber);

    @Query(value = "{ 'cost' : { $ne: '0'} }", delete = true)
    void deleteWhenCostIsGreaterThanZero();
}
