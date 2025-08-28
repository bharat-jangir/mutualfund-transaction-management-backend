package com.rtd.finance_backend.service.serviceImpl;

import com.rtd.finance_backend.exception.ResourceNotFoundException;
import com.rtd.finance_backend.exception.ValidationException;
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
        // Check if PAN already exists
        if (clientMasterRepository.findByPan(client.getPan()).isPresent()) {
            throw new ValidationException("PAN already exists: " + client.getPan());
        }
        
        // Check if mobile already exists
        if (clientMasterRepository.findByMobile(client.getMobile()).isPresent()) {
            throw new ValidationException("Mobile number already exists: " + client.getMobile());
        }
        
        // Check if email already exists
        if (clientMasterRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new ValidationException("Email already exists: " + client.getEmail());
        }
        
        return clientMasterRepository.save(client);
    }

    @Override
    public ClientMaster updateClient(Integer id, ClientMaster updatedClient) {
        ClientMaster existingClient = clientMasterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        
        // Check if PAN is being changed and if it already exists
        if (!existingClient.getPan().equals(updatedClient.getPan())) {
            if (clientMasterRepository.findByPan(updatedClient.getPan()).isPresent()) {
                throw new ValidationException("PAN already exists: " + updatedClient.getPan());
            }
        }
        
        // Check if mobile is being changed and if it already exists
        if (!existingClient.getMobile().equals(updatedClient.getMobile())) {
            if (clientMasterRepository.findByMobile(updatedClient.getMobile()).isPresent()) {
                throw new ValidationException("Mobile number already exists: " + updatedClient.getMobile());
            }
        }
        
        // Check if email is being changed and if it already exists
        if (!existingClient.getEmail().equals(updatedClient.getEmail())) {
            if (clientMasterRepository.findByEmail(updatedClient.getEmail()).isPresent()) {
                throw new ValidationException("Email already exists: " + updatedClient.getEmail());
            }
        }
        
        existingClient.setName(updatedClient.getName());
        existingClient.setDob(updatedClient.getDob());
        existingClient.setPan(updatedClient.getPan());
        existingClient.setTaxStatus(updatedClient.getTaxStatus());
        existingClient.setMobile(updatedClient.getMobile());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setAddress(updatedClient.getAddress());
        existingClient.setIsActive(updatedClient.getIsActive());
        
        return clientMasterRepository.save(existingClient);
    }

    @Override
    public void deleteClient(Integer id) {
        ClientMaster client = clientMasterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        client.setIsActive(false);
        clientMasterRepository.save(client);
    }
    

    @Override
    public Optional<ClientMaster> getClientById(Integer id) {
        return clientMasterRepository.findById(id);
    }

    @Override
    public List<ClientMaster> getAllClients() {
        return clientMasterRepository.findAll();
    }

    @Override
    public List<ClientMaster> getActiveClients() {
        return clientMasterRepository.findByIsActive(true);
    }

    @Override
    public List<ClientMaster> searchClients(String query) {
        return clientMasterRepository.findByNameContainingIgnoreCaseOrPanContainingIgnoreCaseOrMobileContainingIgnoreCaseOrEmailContainingIgnoreCase(
            query, query, query, query);
    }
}
