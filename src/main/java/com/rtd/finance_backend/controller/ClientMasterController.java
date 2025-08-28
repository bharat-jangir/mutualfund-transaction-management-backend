package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.model.ClientMaster;
import com.rtd.finance_backend.service.ClientMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
@Validated
public class ClientMasterController {

    @Autowired
    private ClientMasterService clientMasterService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientMaster>> createClient(@Valid @RequestBody ClientMaster client) {
        ClientMaster createdClient = clientMasterService.createClient(client);
        return ResponseEntity.ok(ApiResponse.success("Client created successfully", createdClient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientMaster>> updateClient(@PathVariable Integer id, @Valid @RequestBody ClientMaster client) {
        ClientMaster updatedClient = clientMasterService.updateClient(id, client);
        return ResponseEntity.ok(ApiResponse.success("Client updated successfully", updatedClient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientMaster>> getClientById(@PathVariable Integer id) {
        ClientMaster client = clientMasterService.getClientById(id)
                .orElseThrow(() -> new com.rtd.finance_backend.exception.ResourceNotFoundException("Client", "id", id));
        return ResponseEntity.ok(ApiResponse.success(client));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientMaster>>> getAllClients() {
        List<ClientMaster> clients = clientMasterService.getAllClients();
        return ResponseEntity.ok(ApiResponse.success("Clients retrieved successfully", clients));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Integer id) {
        clientMasterService.deleteClient(id);
        return ResponseEntity.ok(ApiResponse.success("Client deactivated successfully", null));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ClientMaster>>> getActiveClients() {
        List<ClientMaster> activeClients = clientMasterService.getActiveClients();
        return ResponseEntity.ok(ApiResponse.success("Active clients retrieved successfully", activeClients));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ClientMaster>>> searchClients(@RequestParam String query) {
        List<ClientMaster> clients = clientMasterService.searchClients(query);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", clients));
    }
}
