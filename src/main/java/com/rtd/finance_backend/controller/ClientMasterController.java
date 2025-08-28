package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.model.ClientMaster;
import com.rtd.finance_backend.service.ClientMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientMasterController {

    @Autowired
    private ClientMasterService clientMasterService;

    @PostMapping
    public ResponseEntity<ClientMaster> createClient(@RequestBody ClientMaster client) {
        return ResponseEntity.ok(clientMasterService.createClient(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientMaster> updateClient(@PathVariable Integer id, @RequestBody ClientMaster client) {
        return ResponseEntity.ok(clientMasterService.updateClient(id, client));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientMaster> getClientById(@PathVariable Integer id) {
        return clientMasterService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientMaster>> getAllClients() {
        return ResponseEntity.ok(clientMasterService.getAllClients());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        clientMasterService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
