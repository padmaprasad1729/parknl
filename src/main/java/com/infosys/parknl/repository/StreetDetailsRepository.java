package com.infosys.parknl.repository;

import com.infosys.parknl.model.StreetDetails;
import com.infosys.parknl.model.StreetDetailsDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreetDetailsRepository extends MongoRepository<StreetDetailsDAO, Long> {
    Optional<StreetDetailsDAO> findByStreetName(String parkingStreet);
}
