package com.krishiconnect.service.impl;

import com.krishiconnect.dto.CreateTreeRequest;
import com.krishiconnect.dto.SubmitTreePurchaseRequest;
import com.krishiconnect.dto.TreeDto;
import com.krishiconnect.dto.TreeRequestDto;
import com.krishiconnect.entity.Tree;
import com.krishiconnect.entity.TreeRequest;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.repository.TreeRepository;
import com.krishiconnect.repository.TreeRequestRepository;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.service.TreeService;
import com.krishiconnect.util.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;
    private final TreeRequestRepository treeRequestRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public TreeServiceImpl(TreeRepository treeRepository,
                           TreeRequestRepository treeRequestRepository,
                           UserRepository userRepository,
                           FileStorageService fileStorageService) {
        this.treeRepository = treeRepository;
        this.treeRequestRepository = treeRequestRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreeDto> getAllTrees() {
        return treeRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TreeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TreeDto getTreeById(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));
        return TreeDto.fromEntity(tree);
    }

    @Override
    @Transactional
    public TreeDto postTree(Long adminId, CreateTreeRequest req, MultipartFile image) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        Tree tree = new Tree();
        tree.setName(req.getName());
        tree.setScientificName(req.getScientificName());
        tree.setDescription(req.getDescription());
        tree.setCategory(req.getCategory() != null ? req.getCategory() : "Other");
        tree.setMrp(req.getMrp() != null ? req.getMrp() : BigDecimal.ZERO);
        if (req.getBenefits() != null && !req.getBenefits().isEmpty()) {
            tree.setBenefits(String.join(",", req.getBenefits()));
        }
        tree.setPlantationGuide(req.getPlantationGuide());
        tree.setPostedBy(admin);

        if (image != null && !image.isEmpty()) {
            String filename = fileStorageService.storeFile(image, "trees");
            tree.setImage("uploads/trees/" + filename);
        } else if (req.getImage() != null && !req.getImage().isBlank()) {
            tree.setImage(req.getImage());
        }

        tree = treeRepository.save(tree);
        return TreeDto.fromEntity(tree);
    }

    @Override
    @Transactional
    public void deleteTree(Long treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));
        treeRepository.delete(tree);
    }

    @Override
    @Transactional
    public TreeRequestDto createTreeRequest(Long userId, SubmitTreePurchaseRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tree tree = treeRepository.findById(req.getTreeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));

        BigDecimal totalPrice = tree.getMrp().multiply(BigDecimal.valueOf(req.getQuantity()));

        TreeRequest treeRequest = new TreeRequest();
        treeRequest.setTree(tree);
        treeRequest.setUser(user);
        treeRequest.setQuantity(req.getQuantity());
        treeRequest.setTotalPrice(totalPrice);
        treeRequest.setFullName(req.getFullName());
        treeRequest.setPhone(req.getPhone());
        treeRequest.setShippingAddress(req.getShippingAddress());
        treeRequest.setStatus("Pending");
        treeRequest.setPaymentStatus("Pending");

        treeRequest = treeRequestRepository.save(treeRequest);
        return TreeRequestDto.fromEntity(treeRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreeRequestDto> getMyTreeRequests(Long userId) {
        return treeRequestRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(TreeRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreeRequestDto> getAllTreeRequests() {
        return treeRequestRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TreeRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TreeRequestDto updateTreeRequestStatus(Long requestId, String status, String paymentStatus) {
        TreeRequest request = treeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Tree request not found"));

        if (status != null && !status.isBlank()) {
            request.setStatus(status);
        }
        if (paymentStatus != null && !paymentStatus.isBlank()) {
            request.setPaymentStatus(paymentStatus);
        }

        request = treeRequestRepository.save(request);
        return TreeRequestDto.fromEntity(request);
    }
}
