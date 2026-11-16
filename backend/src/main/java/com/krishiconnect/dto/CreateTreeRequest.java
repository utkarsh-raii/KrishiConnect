package com.krishiconnect.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public class CreateTreeRequest {

    @NotBlank(message = "Tree name is required")
    private String name;

    private String scientificName;

    @NotBlank(message = "Description is required")
    private String description;

    private String category = "Other";
    private BigDecimal mrp = BigDecimal.ZERO;
    private List<String> benefits;
    private String plantationGuide;
    private String image;

    public CreateTreeRequest() {}

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
}
