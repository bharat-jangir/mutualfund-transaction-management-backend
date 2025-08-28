package com.rtd.finance_backend.service;

import com.rtd.finance_backend.model.SchemeMaster;

import java.util.List;
import java.util.Optional;

public interface SchemeMasterService {
    SchemeMaster createScheme(SchemeMaster scheme);
    SchemeMaster updateScheme(Integer id, SchemeMaster scheme);
    void deleteScheme(Integer id);
    Optional<SchemeMaster> getSchemeById(Integer id);
    List<SchemeMaster> getAllSchemes();
}
