package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishiconnect.entity.Tree;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TreeDto {

    @JsonProperty("_id")
    private Long id;

    private String name;
    private String scientificName;
    private String description;
    private String category;
    private BigDecimal mrp;
    private List<String> benefits;
    private String plantationGuide;
    private String image;
    private UserDto postedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TreeDto() {}

    public static TreeDto fromEntity(Tree tree) {
        if (tree == null) return null;
        TreeDto dto = new TreeDto();
        dto.setId(tree.getId());
        dto.setName(tree.getName());
        dto.setScientificName(tree.getScientificName());
        dto.setDescription(tree.getDescription());
        dto.setCategory(tree.getCategory());
        dto.setMrp(tree.getMrp());
        if (tree.getBenefits() != null && !tree.getBenefits().isBlank()) {
            dto.setBenefits(Arrays.asList(tree.getBenefits().split(",")));
        }
        dto.setPlantationGuide(tree.getPlantationGuide());
        dto.setImage(tree.getImage());
        dto.setPostedBy(UserDto.fromEntity(tree.getPostedBy()));
        dto.setCreatedAt(tree.getCreatedAt());
        dto.setUpdatedAt(tree.getUpdatedAt());
        return dto;
    }

    // Expose both id and _id
    @JsonProperty("id")
    public Long getRawId() { return id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }

    public List<String> getBenefits() { return benefits; }
    public void setBenefits(List<String> benefits) { this.benefits = benefits; }

    public String getPlantationGuide() { return plantationGuide; }
    public void setPlantationGuide(String plantationGuide) { this.plantationGuide = plantationGuide; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public UserDto getPostedBy() { return postedBy; }
    public void setPostedBy(UserDto postedBy) { this.postedBy = postedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
