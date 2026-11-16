package com.krishiconnect.controller;

import com.krishiconnect.dto.*;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.AdminService;
import com.krishiconnect.service.TreeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final TreeService treeService;

    public AdminController(AdminService adminService, TreeService treeService) {
        this.adminService = adminService;
        this.treeService = treeService;
    }

    /* ── Dashboard Stats ──────────────────────────────────────────────── */

    @GetMapping(value = {"/dashboard-stats", "/stats"})
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        DashboardStatsDto stats = adminService.getDashboardStats();
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Dashboard stats fetched");
        res.put("data", stats);
        res.put("stats", stats);
        return ResponseEntity.ok(res);
    }

    /* ── User Management ──────────────────────────────────────────────── */

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean isBlocked,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        int pageIndex = Math.max(0, page - 1);
        Page<UserDto> pageResult = adminService.getUsers(
                role, isBlocked, search,
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("users", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        UserDto user = adminService.getUserById(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", user);
        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = {"/block-user/{id}", "/users/{id}/block"}, method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Map<String, Object>> toggleBlockUser(@PathVariable Long id) {
        UserDto user = adminService.toggleBlockUser(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "User block status updated");
        res.put("data", user);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "User deleted successfully");
        return ResponseEntity.ok(res);
    }

    /* ── Transactions ─────────────────────────────────────────────────── */

    @GetMapping("/transactions")
    public ResponseEntity<Map<String, Object>> getTransactions(
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        int pageIndex = Math.max(0, page - 1);
        Page<PurchaseResponseDto> pageResult = adminService.getTransactions(
                paymentStatus,
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("transactions", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/transactions/{id}/verify")
    public ResponseEntity<Map<String, Object>> verifyTransaction(@PathVariable Long id) {
        PurchaseResponseDto purchase = adminService.verifyTransaction(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Transaction verified successfully");
        res.put("data", purchase);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/transactions/{id}/dispute")
    public ResponseEntity<Map<String, Object>> flagDispute(@PathVariable Long id) {
        PurchaseResponseDto purchase = adminService.flagDispute(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Transaction marked as disputed");
        res.put("data", purchase);
        return ResponseEntity.ok(res);
    }

    /* ── Listing Moderation ───────────────────────────────────────────── */

    @GetMapping("/listings")
    public ResponseEntity<Map<String, Object>> getAllListings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String cropName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        int pageIndex = Math.max(0, page - 1);
        Page<CropListingResponse> pageResult = adminService.getAllListings(
                status, cropName,
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("listings", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/listings/{id}")
    public ResponseEntity<Map<String, Object>> removeListing(@PathVariable Long id) {
        adminService.removeListing(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Listing removed by admin");
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/listings/{id}/status")
    public ResponseEntity<Map<String, Object>> updateListingStatus(@PathVariable Long id,
                                                                  @RequestBody Map<String, String> body) {
        String status = body.get("status");
        CropListingResponse listing = adminService.updateListingStatus(id, status);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Listing status updated");
        res.put("data", listing);
        return ResponseEntity.ok(res);
    }

    /* ── Disputes ─────────────────────────────────────────────────────── */

    @GetMapping("/disputes")
    public ResponseEntity<Map<String, Object>> getDisputes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        int pageIndex = Math.max(0, page - 1);
        Page<PurchaseResponseDto> pageResult = adminService.getDisputes(
                PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "updatedAt"))
        );
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", pageResult.getContent());
        res.put("disputes", pageResult.getContent());
        res.put("pagination", new PaginationMeta(pageResult.getTotalElements(), page, limit));
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/disputes/{id}/resolve")
    public ResponseEntity<Map<String, Object>> resolveDispute(@PathVariable Long id,
                                                              @RequestBody Map<String, String> body) {
        String resolution = body.get("resolution");
        PurchaseResponseDto purchase = adminService.resolveDispute(id, resolution);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Dispute resolved as " + resolution);
        res.put("data", purchase);
        return ResponseEntity.ok(res);
    }

    /* ── Tree Marketplace (Admin) ─────────────────────────────────────── */

    @PostMapping(value = "/trees", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> postTreeMultipart(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestPart("name") String name,
            @RequestPart(value = "scientificName", required = false) String scientificName,
            @RequestPart("description") String description,
            @RequestPart(value = "category", required = false) String category,
            @RequestPart(value = "mrp", required = false) String mrp,
            @RequestPart(value = "plantationGuide", required = false) String plantationGuide,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        CreateTreeRequest req = new CreateTreeRequest();
        req.setName(name);
        req.setScientificName(scientificName);
        req.setDescription(description);
        req.setCategory(category != null ? category : "Other");
        if (mrp != null) req.setMrp(new BigDecimal(mrp));
        req.setPlantationGuide(plantationGuide);

        TreeDto tree = treeService.postTree(currentUser.getId(), req, image);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Tree information posted successfully");
        res.put("data", tree);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping(value = "/trees", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> postTreeJson(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CreateTreeRequest req) {

        TreeDto tree = treeService.postTree(currentUser.getId(), req, null);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Tree information posted successfully");
        res.put("data", tree);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/trees")
    public ResponseEntity<Map<String, Object>> getTrees() {
        List<TreeDto> trees = treeService.getAllTrees();
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Trees fetched successfully");
        res.put("data", trees);
        res.put("trees", trees);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/trees/{id}")
    public ResponseEntity<Map<String, Object>> deleteTree(@PathVariable Long id) {
        treeService.deleteTree(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Tree post removed successfully");
        return ResponseEntity.ok(res);
    }

    /* ── Tree Requests (Admin) ────────────────────────────────────────── */

    @GetMapping("/tree-requests")
    public ResponseEntity<Map<String, Object>> getAllTreeRequests() {
        List<TreeRequestDto> requests = treeService.getAllTreeRequests();
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "All tree requests fetched successfully");
        res.put("data", requests);
        res.put("requests", requests);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/tree-requests/{id}/status")
    public ResponseEntity<Map<String, Object>> updateTreeRequestStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        String paymentStatus = body.get("paymentStatus");
        TreeRequestDto updated = treeService.updateTreeRequestStatus(id, status, paymentStatus);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Request updated to " + (status != null ? status : "updated"));
        res.put("data", updated);
        return ResponseEntity.ok(res);
    }
}
