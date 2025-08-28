package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.exception.ResourceNotFoundException;
import com.rtd.finance_backend.exception.ValidationException;
import com.rtd.finance_backend.model.AmcMaster;
import com.rtd.finance_backend.repository.AmcMasterRepository;
import com.rtd.finance_backend.service.AmcMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AmcMasterServiceImpl implements AmcMasterService {

    @Autowired
    private AmcMasterRepository amcMasterRepository;

    @Override
    public AmcMaster createAmc(AmcMaster amc) {
        // Check if AMC code already exists
        if (amcMasterRepository.findByAmcCode(amc.getAmcCode()).isPresent()) {
            throw new ValidationException("AMC code already exists: " + amc.getAmcCode());
        }
        return amcMasterRepository.save(amc);
    }

    @Override
    public AmcMaster updateAmc(Integer id, AmcMaster updatedAmc) {
        AmcMaster existingAmc = amcMasterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AMC", "id", id));
        
        // Check if AMC code is being changed and if it already exists
        if (!existingAmc.getAmcCode().equals(updatedAmc.getAmcCode())) {
            if (amcMasterRepository.findByAmcCode(updatedAmc.getAmcCode()).isPresent()) {
                throw new ValidationException("AMC code already exists: " + updatedAmc.getAmcCode());
            }
        }
        
        existingAmc.setAmcCode(updatedAmc.getAmcCode());
        existingAmc.setAmcName(updatedAmc.getAmcName());
        return amcMasterRepository.save(existingAmc);
    }

    @Override
    public void deleteAmc(Integer id) {
        AmcMaster amc = amcMasterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AMC", "id", id));
        amcMasterRepository.delete(amc);
    }

    @Override
    public Optional<AmcMaster> getAmcById(Integer id) {
        return amcMasterRepository.findById(id);
    }

    @Override
    public List<AmcMaster> getAllAmcs() {
        return amcMasterRepository.findAll();
    }
}
