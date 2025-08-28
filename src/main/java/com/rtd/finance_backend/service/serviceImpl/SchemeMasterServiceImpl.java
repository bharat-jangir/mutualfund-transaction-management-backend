package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.exception.ResourceNotFoundException;
import com.rtd.finance_backend.exception.ValidationException;
import com.rtd.finance_backend.model.SchemeMaster;
import com.rtd.finance_backend.repository.SchemeMasterRepository;
import com.rtd.finance_backend.service.SchemeMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchemeMasterServiceImpl implements SchemeMasterService {

    @Autowired
    private SchemeMasterRepository schemeMasterRepository;

    @Override
    public SchemeMaster createScheme(SchemeMaster scheme) {
        // Check if ISIN code already exists
        if (schemeMasterRepository.findByIsinCode(scheme.getIsinCode()).isPresent()) {
            throw new ValidationException("ISIN code already exists: " + scheme.getIsinCode());
        }
        
        // Check if scheme code already exists
        if (schemeMasterRepository.findBySchemeCode(scheme.getSchemeCode()).isPresent()) {
            throw new ValidationException("Scheme code already exists: " + scheme.getSchemeCode());
        }
        
        return schemeMasterRepository.save(scheme);
    }

    @Override
    public SchemeMaster updateScheme(Integer id, SchemeMaster updatedScheme) {
        SchemeMaster existingScheme = schemeMasterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));
        
        // Check if ISIN code is being changed and if it already exists
        if (!existingScheme.getIsinCode().equals(updatedScheme.getIsinCode())) {
            if (schemeMasterRepository.findByIsinCode(updatedScheme.getIsinCode()).isPresent()) {
                throw new ValidationException("ISIN code already exists: " + updatedScheme.getIsinCode());
            }
        }
        
        // Check if scheme code is being changed and if it already exists
        if (!existingScheme.getSchemeCode().equals(updatedScheme.getSchemeCode())) {
            if (schemeMasterRepository.findBySchemeCode(updatedScheme.getSchemeCode()).isPresent()) {
                throw new ValidationException("Scheme code already exists: " + updatedScheme.getSchemeCode());
            }
        }
        
        existingScheme.setAmc(updatedScheme.getAmc());
        existingScheme.setSchemeCode(updatedScheme.getSchemeCode());
        existingScheme.setSchemeName(updatedScheme.getSchemeName());
        existingScheme.setIsinCode(updatedScheme.getIsinCode());
        
        return schemeMasterRepository.save(existingScheme);
    }

    @Override
    public void deleteScheme(Integer id) {
        SchemeMaster scheme = schemeMasterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));
        schemeMasterRepository.delete(scheme);
    }

    @Override
    public Optional<SchemeMaster> getSchemeById(Integer id) {
        return schemeMasterRepository.findById(id);
    }

    @Override
    public List<SchemeMaster> getAllSchemes() {
        return schemeMasterRepository.findAll();
    }
}
