package com.rtd.finance_backend.service;

import com.rtd.finance_backend.model.ClientMaster;

import java.util.List;
import java.util.Optional;

public interface ClientMasterService {
    ClientMaster createClient(ClientMaster client);
    ClientMaster updateClient(Integer id, ClientMaster client);
    void deleteClient(Integer id);
    Optional<ClientMaster> getClientById(Integer id);
    List<ClientMaster> getAllClients();
    List<ClientMaster> getActiveClients();
    List<ClientMaster> searchClients(String query);
}
