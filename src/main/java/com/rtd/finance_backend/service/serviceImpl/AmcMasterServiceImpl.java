package com.rtd.finance_backend.service.serviceImpl;

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
        return amcMasterRepository.save(amc);
    }

    @Override
    public AmcMaster updateAmc(Integer id, AmcMaster updatedAmc) {
        return amcMasterRepository.findById(id).map(amc -> {
            amc.setAmcCode(updatedAmc.getAmcCode());
            amc.setAmcName(updatedAmc.getAmcName());
            return amcMasterRepository.save(amc);
        }).orElseThrow(() -> new RuntimeException("AMC not found with id " + id));
    }

    @Override
    public void deleteAmc(Integer id) {
        amcMasterRepository.deleteById(id);
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
