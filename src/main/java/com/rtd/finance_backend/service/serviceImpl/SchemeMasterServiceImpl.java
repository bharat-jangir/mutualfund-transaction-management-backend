package com.rtd.finance_backend.service.serviceImpl;

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
        return schemeMasterRepository.save(scheme);
    }

    @Override
    public SchemeMaster updateScheme(Integer id, SchemeMaster updatedScheme) {
        return schemeMasterRepository.findById(id).map(scheme -> {
            scheme.setAmc(updatedScheme.getAmc());
            scheme.setSchemeCode(updatedScheme.getSchemeCode());
            scheme.setSchemeName(updatedScheme.getSchemeName());
            scheme.setIsinCode(updatedScheme.getIsinCode());
            return schemeMasterRepository.save(scheme);
        }).orElseThrow(() -> new RuntimeException("Scheme not found with id " + id));
    }

    @Override
    public void deleteScheme(Integer id) {
        schemeMasterRepository.deleteById(id);
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
