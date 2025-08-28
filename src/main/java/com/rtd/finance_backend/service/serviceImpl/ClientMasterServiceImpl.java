package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.model.ClientMaster;
import com.rtd.finance_backend.repository.ClientMasterRepository;
import com.rtd.finance_backend.service.ClientMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientMasterServiceImpl implements ClientMasterService {

    @Autowired
    private ClientMasterRepository clientMasterRepository;

    @Override
    public ClientMaster createClient(ClientMaster client) {
        return clientMasterRepository.save(client);
    }

    @Override
    public ClientMaster updateClient(Integer id, ClientMaster updatedClient) {
        return clientMasterRepository.findById(id).map(client -> {
            client.setName(updatedClient.getName());
            client.setDob(updatedClient.getDob());
            client.setPan(updatedClient.getPan());
            client.setTaxStatus(updatedClient.getTaxStatus());
            client.setMobile(updatedClient.getMobile());
            client.setEmail(updatedClient.getEmail());
            client.setAddress(updatedClient.getAddress());
            client.setIsActive(updatedClient.getIsActive());
            return clientMasterRepository.save(client);
        }).orElseThrow(() -> new RuntimeException("Client not found with id " + id));
    }

    @Override
    public void deleteClient(Integer id) {
        Optional<ClientMaster> optionalClient = clientMasterRepository.findById(id);
        if (optionalClient.isPresent()) {
            ClientMaster client = optionalClient.get();
            client.setIsActive(false); // assuming the field is named 'isActive'
            clientMasterRepository.save(client); // this performs the update
        } else {
            throw new RuntimeException("Client not found with ID: " + id);
        }
    }
    

    @Override
    public Optional<ClientMaster> getClientById(Integer id) {
        return clientMasterRepository.findById(id);
    }

    @Override
    public List<ClientMaster> getAllClients() {
        return clientMasterRepository.findAll();
    }
}
