package com.rtd.finance_backend.repository;

import com.rtd.finance_backend.model.ClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientMasterRepository extends JpaRepository<ClientMaster, Integer> {
    Optional<ClientMaster> findByPan(String pan);
    Optional<ClientMaster> findByMobile(String mobile);
    Optional<ClientMaster> findByEmail(String email);
    List<ClientMaster> findByIsActive(Boolean isActive);
    List<ClientMaster> findByTaxStatus(ClientMaster.TaxStatus taxStatus);
    List<ClientMaster> findByNameContainingIgnoreCaseOrPanContainingIgnoreCaseOrMobileContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String name, String pan, String mobile, String email);
}
