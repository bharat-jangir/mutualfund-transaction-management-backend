package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.model.AmcMaster;
import com.rtd.finance_backend.service.AmcMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amcs")
@CrossOrigin("*")
public class AmcMasterController {

    @Autowired
    private AmcMasterService amcMasterService;

    @PostMapping
    public ResponseEntity<AmcMaster> createAmc(@RequestBody AmcMaster amc) {
        return ResponseEntity.ok(amcMasterService.createAmc(amc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmcMaster> updateAmc(@PathVariable Integer id, @RequestBody AmcMaster amc) {
        return ResponseEntity.ok(amcMasterService.updateAmc(id, amc));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmcMaster> getAmcById(@PathVariable Integer id) {
        return amcMasterService.getAmcById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AmcMaster>> getAllAmcs() {
        return ResponseEntity.ok(amcMasterService.getAllAmcs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmc(@PathVariable Integer id) {
        amcMasterService.deleteAmc(id);
        return ResponseEntity.noContent().build();
    }
}
