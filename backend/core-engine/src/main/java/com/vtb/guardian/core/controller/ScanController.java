package com.vtb.guardian.core.controller;

import com.vtb.guardian.core.model.dto.request.CreateScanRequest;
import com.vtb.guardian.core.model.dto.response.ScanResponse;
import com.vtb.guardian.core.service.ScanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scans")
@RequiredArgsConstructor
@Tag(name = "Scans", description = "API Security Scanning endpoints")
public class ScanController {

    private final ScanService scanService;

    @PostMapping
    @Operation(summary = "Create new scan", description = "Start a new API security scan")
    public ResponseEntity<ScanResponse> createScan(@Valid @RequestBody CreateScanRequest request) {
        ScanResponse response = scanService.createScan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List scans", description = "Get paginated list of scans")
    public ResponseEntity<Page<ScanResponse>> listScans(
            @RequestParam(required = false) UUID projectId,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        Page<ScanResponse> scans = scanService.listScans(projectId, status, pageable);
        return ResponseEntity.ok(scans);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get scan by ID", description = "Retrieve scan details")
    public ResponseEntity<ScanResponse> getScan(@PathVariable UUID id) {
        ScanResponse scan = scanService.getScan(id);
        return ResponseEntity.ok(scan);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete scan", description = "Delete a scan and its results")
    public ResponseEntity<Void> deleteScan(@PathVariable UUID id) {
        scanService.deleteScan(id);
        return ResponseEntity.noContent().build();
    }
}

