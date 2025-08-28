package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.dto.ApiResponse;
import com.rtd.finance_backend.model.SchemeMaster;
import com.rtd.finance_backend.service.SchemeMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/schemes")
@CrossOrigin("*")
@Validated
public class SchemeMasterController {

    @Autowired
    private SchemeMasterService schemeMasterService;

    @PostMapping
    public ResponseEntity<ApiResponse<SchemeMaster>> createScheme(@Valid @RequestBody SchemeMaster scheme) {
        SchemeMaster createdScheme = schemeMasterService.createScheme(scheme);
        return ResponseEntity.ok(ApiResponse.success("Scheme created successfully", createdScheme));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SchemeMaster>> updateScheme(@PathVariable Integer id, @Valid @RequestBody SchemeMaster scheme) {
        SchemeMaster updatedScheme = schemeMasterService.updateScheme(id, scheme);
        return ResponseEntity.ok(ApiResponse.success("Scheme updated successfully", updatedScheme));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchemeMaster>> getSchemeById(@PathVariable Integer id) {
        SchemeMaster scheme = schemeMasterService.getSchemeById(id)
                .orElseThrow(() -> new com.rtd.finance_backend.exception.ResourceNotFoundException("Scheme", "id", id));
        return ResponseEntity.ok(ApiResponse.success(scheme));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SchemeMaster>>> getAllSchemes() {
        List<SchemeMaster> schemes = schemeMasterService.getAllSchemes();
        return ResponseEntity.ok(ApiResponse.success("Schemes retrieved successfully", schemes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScheme(@PathVariable Integer id) {
        schemeMasterService.deleteScheme(id);
        return ResponseEntity.ok(ApiResponse.success("Scheme deleted successfully", null));
    }
}
