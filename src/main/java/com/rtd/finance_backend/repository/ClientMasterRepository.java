package com.rtd.finance_backend.repository;

import com.rtd.finance_backend.model.ClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientMasterRepository extends JpaRepository<ClientMaster, Integer> {
}
