package com.krishiconnect.controller;

import com.krishiconnect.dto.PurchaseResponseDto;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/farmer-orders")
@PreAuthorize("hasRole('FARMER')")
public class FarmerOrderController {

    private final FarmerService farmerService;

    public FarmerOrderController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPurchaseRequests(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<PurchaseResponseDto> requests = farmerService.getPurchaseRequests(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("requests", requests);
        res.put("data", requests);
        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Map<String, Object>> updateRequestStatus(@AuthenticationPrincipal UserPrincipal currentUser,
                                                                   @PathVariable Long id,
                                                                   @RequestBody Map<String, String> body) {
        String status = body.get("status");
        PurchaseResponseDto updated = farmerService.updatePurchaseRequestStatus(currentUser.getId(), id, status);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Request status updated successfully");
        res.put("request", updated);
        res.put("data", updated);
        return ResponseEntity.ok(res);
    }
}
