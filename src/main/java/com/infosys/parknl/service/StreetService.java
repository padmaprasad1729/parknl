package com.infosys.parknl.service;

import com.infosys.parknl.exception.StreetAlreadyFoundException;
import com.infosys.parknl.exception.StreetNotFoundException;
import com.infosys.parknl.model.StreetDetails;
import com.infosys.parknl.model.StreetDetailsDAO;
import com.infosys.parknl.repository.StreetDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StreetService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final StreetDetailsRepository streetRepo;

    private final SequenceGeneratorService seqService;

    public StreetService(StreetDetailsRepository streetRepo, SequenceGeneratorService seqService) {
        this.streetRepo = streetRepo;
        this.seqService = seqService;
    }

    public StreetDetails addStreetDetails(StreetDetails input) {
        failIfStreetAlreadyFound(input);
        StreetDetailsDAO dao = mapFromDtoToDao(input);
        dao.setId(seqService.generateSequence(StreetDetailsDAO.SEQUENCE_NAME));
        return mapFromDaoToDto(streetRepo.save(dao));
    }

    public List<StreetDetails> readAllStreetDetails() {
        List<StreetDetailsDAO> daoList = streetRepo.findAll();
        return daoList.stream().map(this::mapFromDaoToDto).collect(Collectors.toList());
    }

    public StreetDetails findByStreetName(String streetName) {
        return mapFromDaoToDto(getStreetOrFailOnEmpty(streetName));
    }

    public StreetDetails updateStreetDetails(String streetName, StreetDetails input) {
        StreetDetailsDAO streetDetailsDAO = getStreetOrFailOnEmpty(streetName);

        if (!input.getStreetName().equals(streetDetailsDAO.getStreetName())) {
            failIfStreetAlreadyFound(input);
            streetDetailsDAO.setStreetName(input.getStreetName());
        }
        if (!input.getPriceInCentPerMinute().equals(streetDetailsDAO.getPriceInCentPerMinute())) {
            streetDetailsDAO.setPriceInCentPerMinute(input.getPriceInCentPerMinute());
        }

        streetRepo.save(streetDetailsDAO);

        return mapFromDaoToDto(streetDetailsDAO);
    }

    public void removeStreet(String streetName) {
        streetRepo.delete(getStreetOrFailOnEmpty(streetName));
    }

    public void failIfStreetAlreadyFound(StreetDetails input) {
        Optional<StreetDetailsDAO> streetDetailsDAO = streetRepo.findByStreetName(input.getStreetName());
        if (streetDetailsDAO.isPresent()) {
            throw new StreetAlreadyFoundException("Street already found with name " + input.getStreetName());
        }
    }

    public StreetDetailsDAO getStreetOrFailOnEmpty(String streetName) {
        Optional<StreetDetailsDAO> dao = streetRepo.findByStreetName(streetName);
        if (dao.isEmpty()) {
            throw new StreetNotFoundException("Street Details Not Found for the street name " + streetName);
        }
        return dao.get();
    }

    private StreetDetails mapFromDaoToDto(StreetDetailsDAO dao) {
        return StreetDetails.builder()
                .streetName(dao.getStreetName())
                .priceInCentPerMinute(dao.getPriceInCentPerMinute())
                .build();
    }

    private StreetDetailsDAO mapFromDtoToDao(StreetDetails dto) {
        return StreetDetailsDAO.builder()
                .streetName(dto.getStreetName())
                .priceInCentPerMinute(dto.getPriceInCentPerMinute())
                .build();
    }


}
