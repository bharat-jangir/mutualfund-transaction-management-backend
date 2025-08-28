package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.model.AmcMaster;
import com.rtd.finance_backend.service.AmcMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/amcs")
@CrossOrigin("*")
@Validated
public class AmcMasterController {

    @Autowired
    private AmcMasterService amcMasterService;

    @PostMapping
    public ResponseEntity<ApiResponse<AmcMaster>> createAmc(@Valid @RequestBody AmcMaster amc) {
        AmcMaster createdAmc = amcMasterService.createAmc(amc);
        return ResponseEntity.ok(ApiResponse.success("AMC created successfully", createdAmc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AmcMaster>> updateAmc(@PathVariable Integer id, @Valid @RequestBody AmcMaster amc) {
        AmcMaster updatedAmc = amcMasterService.updateAmc(id, amc);
        return ResponseEntity.ok(ApiResponse.success("AMC updated successfully", updatedAmc));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AmcMaster>> getAmcById(@PathVariable Integer id) {
        AmcMaster amc = amcMasterService.getAmcById(id)
                .orElseThrow(() -> new com.rtd.finance_backend.exception.ResourceNotFoundException("AMC", "id", id));
        return ResponseEntity.ok(ApiResponse.success(amc));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AmcMaster>>> getAllAmcs() {
        List<AmcMaster> amcs = amcMasterService.getAllAmcs();
        return ResponseEntity.ok(ApiResponse.success("AMCs retrieved successfully", amcs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAmc(@PathVariable Integer id) {
        amcMasterService.deleteAmc(id);
        return ResponseEntity.ok(ApiResponse.success("AMC deleted successfully", null));
    }
}
