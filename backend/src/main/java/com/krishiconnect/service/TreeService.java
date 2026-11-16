package com.krishiconnect.service;

import com.krishiconnect.dto.CreateTreeRequest;
import com.krishiconnect.dto.SubmitTreePurchaseRequest;
import com.krishiconnect.dto.TreeDto;
import com.krishiconnect.dto.TreeRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TreeService {

    List<TreeDto> getAllTrees();

    TreeDto getTreeById(Long id);

    TreeDto postTree(Long adminId, CreateTreeRequest req, MultipartFile image);

    void deleteTree(Long treeId);

    TreeRequestDto createTreeRequest(Long userId, SubmitTreePurchaseRequest req);

    List<TreeRequestDto> getMyTreeRequests(Long userId);

    List<TreeRequestDto> getAllTreeRequests();

    TreeRequestDto updateTreeRequestStatus(Long requestId, String status, String paymentStatus);
}
