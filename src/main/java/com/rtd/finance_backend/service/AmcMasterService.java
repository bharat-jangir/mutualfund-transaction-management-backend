package com.rtd.finance_backend.service;

import com.rtd.finance_backend.model.AmcMaster;

import java.util.List;
import java.util.Optional;

public interface AmcMasterService {
    AmcMaster createAmc(AmcMaster amc);
    AmcMaster updateAmc(Integer id, AmcMaster amc);
    void deleteAmc(Integer id);
    Optional<AmcMaster> getAmcById(Integer id);
    List<AmcMaster> getAllAmcs();
}
