package com.krishiconnect.controller;

import com.krishiconnect.dto.SubmitTreePurchaseRequest;
import com.krishiconnect.dto.TreeDto;
import com.krishiconnect.dto.TreeRequestDto;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.TreeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trees")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTrees() {
        List<TreeDto> trees = treeService.getAllTrees();
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Trees fetched successfully");
        res.put("trees", trees);
        res.put("data", trees);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTreeById(@PathVariable Long id) {
        TreeDto tree = treeService.getTreeById(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", tree);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> createTreeRequest(@AuthenticationPrincipal UserPrincipal currentUser,
                                                                 @Valid @RequestBody SubmitTreePurchaseRequest req) {
        TreeRequestDto treeRequest = treeService.createTreeRequest(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Tree purchase request submitted successfully");
        res.put("request", treeRequest);
        res.put("data", treeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<Map<String, Object>> getMyTreeRequests(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<TreeRequestDto> requests = treeService.getMyTreeRequests(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Your tree requests fetched successfully");
        res.put("requests", requests);
        res.put("data", requests);
        return ResponseEntity.ok(res);
    }
}
