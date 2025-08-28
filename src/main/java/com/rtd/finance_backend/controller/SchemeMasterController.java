package com.rtd.finance_backend.controller;

import com.rtd.finance_backend.model.SchemeMaster;
import com.rtd.finance_backend.service.SchemeMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schemes")
@CrossOrigin("*")
public class SchemeMasterController {

    @Autowired
    private SchemeMasterService schemeMasterService;

    @PostMapping
    public ResponseEntity<SchemeMaster> createScheme(@RequestBody SchemeMaster scheme) {
        return ResponseEntity.ok(schemeMasterService.createScheme(scheme));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchemeMaster> updateScheme(@PathVariable Integer id, @RequestBody SchemeMaster scheme) {
        return ResponseEntity.ok(schemeMasterService.updateScheme(id, scheme));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchemeMaster> getSchemeById(@PathVariable Integer id) {
        return schemeMasterService.getSchemeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SchemeMaster>> getAllSchemes() {
        return ResponseEntity.ok(schemeMasterService.getAllSchemes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheme(@PathVariable Integer id) {
        schemeMasterService.deleteScheme(id);
        return ResponseEntity.noContent().build();
    }
}
