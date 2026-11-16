package com.krishiconnect.config;

import com.krishiconnect.entity.*;
import com.krishiconnect.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Order(10)
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final CropListingRepository cropListingRepository;
    private final ServiceListingRepository serviceListingRepository;
    private final HarvestRequirementRepository harvestRequirementRepository;
    private final TreeRepository treeRepository;
    private final FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@fhc.com}")
    private String configuredAdminEmail;

    public DataSeeder(UserRepository userRepository,
                      CropListingRepository cropListingRepository,
                      ServiceListingRepository serviceListingRepository,
                      HarvestRequirementRepository harvestRequirementRepository,
                      TreeRepository treeRepository,
                      FeedbackRepository feedbackRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cropListingRepository = cropListingRepository;
        this.serviceListingRepository = serviceListingRepository;
        this.harvestRequirementRepository = harvestRequirementRepository;
        this.treeRepository = treeRepository;
        this.feedbackRepository = feedbackRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        seedCropListings();
        seedTrees();
        seedServices();
        seedHarvestRequirements();
        seedFeedbacks();
    }

    private void seedUsers() {
        // 1. Admin (check if admin already exists via AuthServiceImpl bootstrap)
        if (userRepository.countByRole("admin") == 0) {
            createUserIfMissing("FHC Admin", configuredAdminEmail != null ? configuredAdminEmail : "admin@fhc.com",
                    "Admin@1234", "9000000001", "admin",
                    "New Delhi, India", "Farmer Harvest Connect HQ", null, null,
                    "Platform Administrator for KrishiConnect agricultural marketplace.");
        }

        // 2. Demo Farmer
        createUserIfMissing("Ramesh Patel", "farmer@demo.com", "demo123", "9876543210", "farmer",
                "Indore, Madhya Pradesh", null, null, null,
                "Experienced organic farmer with 20+ years cultivating wheat, soybean, and pulses.");

        // 3. Additional Farmers
        createUserIfMissing("Suresh Kumar", "suresh@farmer.com", "Farmer@123", "9876543211", "farmer",
                "Vidisha, Madhya Pradesh", null, null, null,
                "Fourth-generation paddy and pulse cultivator utilizing sustainable drip irrigation.");

        createUserIfMissing("Kiran Desai", "kiran@farmer.com", "Farmer@123", "9876543212", "farmer",
                "Rajkot, Gujarat", null, null, null,
                "Cotton, groundnut, and oilseed grower with certified organic certification.");

        // 4. Demo Buyer
        createUserIfMissing("Priya Mehta", "priya@buyer.com", "Buyer@123", "9800001111", "buyer",
                "Mumbai, Maharashtra", "Mehta Agro Commodities", null, null,
                "Wholesale agricultural commodities buyer, bulk food processor, and exporter.");

        createUserIfMissing("Anil Sharma", "anil@buyer.com", "Buyer@123", "9800002222", "buyer",
                "Pune, Maharashtra", "Green Valley Foods Pvt Ltd", null, null,
                "Direct farm-to-retail supply chain aggregator for supermarkets and retail outlets.");

        // 5. Demo Service Provider
        createUserIfMissing("Suresh Logistics", "provider@demo.com", "demo123", "9812345670", "provider",
                "Indore, Madhya Pradesh", "Suresh Logistics & Transport", "transport", 5,
                "Reliable heavy agricultural logistics, GPS-tracked multi-axle freight trucks.");

        createUserIfMissing("Ravi Agri Services", "provider2@fhc.com", "Provider@123", "9812345671", "provider",
                "Sehore, Madhya Pradesh", "Ravi Harvester & Manpower Co.", "both", 3,
                "High-capacity combine harvesters, threshers, and skilled seasonal harvesting workforce.");
    }

    private User createUserIfMissing(String name, String email, String password, String phone,
                                     String role, String location, String businessName,
                                     String serviceType, Integer vehicleCount, String bio) {
        String normalizedEmail = email.toLowerCase().trim();
        Optional<User> byEmail = userRepository.findByEmailIgnoreCase(normalizedEmail);
        if (byEmail.isPresent()) {
            return byEmail.get();
        }
        Optional<User> byPhone = userRepository.findByPhone(phone);
        if (byPhone.isPresent()) {
            return byPhone.get();
        }

        logger.info("🌱 Seeding user: {} ({})", name, normalizedEmail);
        User user = new User();
        user.setName(name);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(role);
        user.setIsVerified(true);
        user.setIsBlocked(false);
        user.setLocation(location);
        user.setBusinessName(businessName);
        user.setServiceType(serviceType);
        user.setVehicleCount(vehicleCount != null ? vehicleCount : 0);
        user.setBio(bio);
        user.setAverageRating(BigDecimal.valueOf(4.8));
        user.setTotalRatings(12);
        return userRepository.save(user);
    }

    private void seedCropListings() {
        if (cropListingRepository.count() > 0) return;

        User farmer1 = userRepository.findByEmailIgnoreCase("farmer@demo.com").orElse(null);
        User farmer2 = userRepository.findByEmailIgnoreCase("suresh@farmer.com").orElse(null);
        User farmer3 = userRepository.findByEmailIgnoreCase("kiran@farmer.com").orElse(null);

        if (farmer1 == null) return;

        logger.info("🌾 Seeding initial Crop Listings...");

        // Listing 1: Sharbati Wheat
        createCropListing(farmer1, "Sharbati Golden Wheat", "Cereals",
                new BigDecimal("150"), "Quintal", new BigDecimal("2800"), "per Quintal", true,
                "Village Sanwer", "Indore", "Madhya Pradesh", "453551",
                "Rabi", "A+", "9876543210",
                "Premium quality Sharbati wheat, organically grown with zero synthetic fertilizers. High protein content, ideal for chapatis.");

        // Listing 2: Organic Soybean
        createCropListing(farmer1, "Yellow Organic Soybean", "Oilseeds",
                new BigDecimal("90"), "Quintal", new BigDecimal("4650"), "per Quintal", true,
                "Village Depalpur", "Indore", "Madhya Pradesh", "453115",
                "Kharif", "A", "9876543210",
                "High oil content JS-9560 yellow soybean, thoroughly cleaned, sorted, and moisture-controlled at 10%.");

        // Listing 3: Basmati Rice
        if (farmer2 != null) {
            createCropListing(farmer2, "Traditional Basmati Paddy 1121", "Cereals",
                    new BigDecimal("220"), "Quintal", new BigDecimal("3500"), "per Quintal", false,
                    "Village Kurwai", "Vidisha", "Madhya Pradesh", "464224",
                    "Kharif", "A+", "9876543211",
                    "Authentic aromatic 1121 Basmati paddy, long grain, harvested at peak maturity from fertile alluvial soil.");

            createCropListing(farmer2, "Desi Chana (Chickpeas)", "Pulses",
                    new BigDecimal("75"), "Quintal", new BigDecimal("5400"), "per Quintal", true,
                    "Village Basoda", "Vidisha", "Madhya Pradesh", "464221",
                    "Rabi", "A", "9876543211",
                    "Unpolished Desi Bengal gram / chana, sun-dried, pesticide-free, machine cleaned.");
        }

        // Listing 4: Cotton & Groundnut
        if (farmer3 != null) {
            createCropListing(farmer3, "Shankar-6 Long Staple Cotton", "Cash Crops",
                    new BigDecimal("130"), "Quintal", new BigDecimal("6900"), "per Quintal", true,
                    "Gondal Road", "Rajkot", "Gujarat", "360004",
                    "Kharif", "A+", "9876543212",
                    "Premium Shankar-6 raw seed cotton with 29mm staple length and high micronaire value. Hand-picked cleanly.");

            createCropListing(farmer3, "Bold Groundnut (Peanut)", "Oilseeds",
                    new BigDecimal("85"), "Quintal", new BigDecimal("6200"), "per Quintal", true,
                    "Jetpur Highway", "Rajkot", "Gujarat", "360360",
                    "Kharif", "A", "9876543212",
                    "Bold variety Virginia groundnut pods, 50-60 count, fresh harvest, excellent oil yield.");
        }

        logger.info("✅ Crop listings seeded successfully");
    }

    private void createCropListing(User farmer, String cropName, String category,
                                  BigDecimal qty, String qtyUnit, BigDecimal price, String priceUnit,
                                  boolean negotiable, String address, String district, String state,
                                  String pincode, String season, String grade, String phone, String desc) {
        CropListing c = new CropListing();
        c.setFarmer(farmer);
        c.setCropName(cropName);
        c.setCategory(category);
        c.setQuantityAmount(qty);
        c.setQuantityUnit(qtyUnit);
        c.setPriceAmount(price);
        c.setPriceUnit(priceUnit);
        c.setPriceNegotiable(negotiable);
        c.setAddress(address);
        c.setDistrict(district);
        c.setState(state);
        c.setPincode(pincode);
        c.setHarvestSeason(season);
        c.setHarvestGrade(grade);
        c.setHarvestDate(LocalDate.now().minusWeeks(2));
        c.setContactPhone(phone);
        c.setDescription(desc);
        c.setStatus("active");
        c.setExpiresAt(LocalDateTime.now().plusMonths(2));
        cropListingRepository.save(c);
    }

    private void seedTrees() {
        if (treeRepository.count() > 0) return;

        User admin = userRepository.findByEmailIgnoreCase(configuredAdminEmail != null ? configuredAdminEmail : "admin@fhc.com")
                .or(() -> userRepository.findAll().stream().filter(u -> "admin".equals(u.getRole())).findFirst())
                .orElse(null);

        if (admin == null) return;

        logger.info("🌳 Seeding Tree Marketplace catalog...");

        createTree(admin, "Neem Tree", "Azadirachta indica", "Medicinal", new BigDecimal("299.00"),
                "Neem tree is globally celebrated for its medicinal, antibacterial, and environmental purification properties. Acts as a natural biopesticide.",
                "Purifies air, natural bio-pesticide, medicinal leaves and bark, deep root system.",
                "Dig a 2ft x 2ft pit, mix soil with organic vermicompost, water moderately twice weekly.",
                "https://images.unsplash.com/photo-1542273917363-3b1817f69a2d");

        createTree(admin, "Mango Tree (Kesar)", "Mangifera indica", "Fruit", new BigDecimal("499.00"),
                "High-yielding graft of the famous Kesar mango, renowned for its intense fragrance, saffron color, and rich sweet taste.",
                "Delicious sweet fruit harvest in 2-3 years, dense canopy, excellent shade provider.",
                "Requires full direct sunlight and well-draining loamy soil. Water deeply once a week during flowering.",
                "https://images.unsplash.com/photo-1553279768-865429fa0078");

        createTree(admin, "Banyan Tree", "Ficus benghalensis", "Shade", new BigDecimal("699.00"),
                "The national tree of India, majestic and sacred. Spreads extensive aerial roots that mature into massive support trunks.",
                "Colossal shade canopy, supports hundreds of bird and pollinator species, extreme longevity (centuries).",
                "Plant in wide open rural spaces away from building foundations. Hardy and drought-tolerant once rooted.",
                "https://images.unsplash.com/photo-1473448912268-2022ce9509d8");

        createTree(admin, "Peepal Tree", "Ficus religiosa", "Environmental", new BigDecimal("399.00"),
                "Ecologically vital sacred fig tree capable of releasing oxygen around the clock, celebrated for air quality improvement.",
                "Continuous oxygen release, carbon sequestering champion, high cultural and spiritual heritage.",
                "Requires minimal water once established. Prefers well-draining soil and plenty of sun.",
                "https://images.unsplash.com/photo-1502082553048-f009c37129b9");

        createTree(admin, "Coconut Palm", "Cocos nucifera", "Fruit", new BigDecimal("799.00"),
                "Dwarf hybrid high-yield coconut palm, perfect for farm perimeters and coastal or humid regions.",
                "Year-round tender coconut water and copra, hurricane resistant, beautiful tropical aesthetic.",
                "Plant in sandy loam, ensure consistent moisture and potash-rich organic feeding.",
                "https://images.unsplash.com/photo-1512341689857-198e7e2f3ca8");

        createTree(admin, "Guava Tree (Allahabad Safeda)", "Psidium guajava", "Fruit", new BigDecimal("350.00"),
                "Popular Indian fruit tree that yields abundant, large round guavas with white, sweet, fragrant pulp packed with Vitamin C.",
                "Quick fruiting within 1-2 years, rich in Vitamin C and fiber, drought tolerant.",
                "Prune dead branches after seasonal fruiting to stimulate heavy flowering in the next cycle.",
                "https://images.unsplash.com/photo-1536511135899-7323b2075775");

        createTree(admin, "Teak Tree (Sagwan)", "Tectona grandis", "Timber", new BigDecimal("999.00"),
                "Premier commercial timber tree known for durable, golden-brown hardwood with natural weather and termite resistance.",
                "High commercial value timber crop, ideal agroforestry investment, long-term asset.",
                "Plant in deep, fertile, moist but well-drained soil with 2m x 2m spacing for timber stands.",
                "https://images.unsplash.com/photo-1448375240586-882707db888b");

        createTree(admin, "Bamboo Clump (Bambusa balcooa)", "Bambusa balcooa", "Eco", new BigDecimal("250.00"),
                "Fastest growing woody plant on Earth. Ideal for windbreaks, soil erosion control, and biomass production.",
                "Rapid biomass growth, prevents soil erosion along farm boundaries, versatile farm building material.",
                "Thrives along water channels and field borders. Drought hardy once clumps are established.",
                "https://images.unsplash.com/photo-1528183429752-a97d0bf99b5a");

        logger.info("✅ Tree catalog seeded successfully");
    }

    private void createTree(User admin, String name, String scientific, String category,
                            BigDecimal mrp, String desc, String benefits, String guide, String image) {
        Tree t = new Tree();
        t.setName(name);
        t.setScientificName(scientific);
        t.setCategory(category);
        t.setMrp(mrp);
        t.setDescription(desc);
        t.setBenefits(benefits);
        t.setPlantationGuide(guide);
        t.setImage(image);
        t.setPostedBy(admin);
        treeRepository.save(t);
    }

    private void seedServices() {
        if (serviceListingRepository.count() > 0) return;

        User provider1 = userRepository.findByEmailIgnoreCase("provider@demo.com").orElse(null);
        User provider2 = userRepository.findByEmailIgnoreCase("provider2@fhc.com").orElse(null);

        if (provider1 == null) return;

        logger.info("🚛 Seeding Service Listings...");

        // Service 1: Heavy Freight Truck
        ServiceListing s1 = new ServiceListing();
        s1.setProvider(provider1);
        s1.setServiceType("transport");
        s1.setVehicleType("10-Ton Multi-Axle Freight Truck");
        s1.setVehicleCount(3);
        s1.setVehicleCapacity("10 Ton");
        s1.setVehicleRegistrationNo("MP-09-AB-1234");
        s1.setVehicleDescription("All-weather covered container trucks equipped with GPS tracking for secure agricultural haulage.");
        s1.setPricePerDay(new BigDecimal("4200.00"));
        s1.setIsAvailable(true);
        s1.setIsActive(true);
        s1.setServiceStates("Madhya Pradesh, Maharashtra, Gujarat");
        s1.setServiceDistricts("Indore, Ujjain, Dewas, Sehore, Bhopal");
        s1.setServiceRadius(150);
        s1.setRating(BigDecimal.valueOf(4.9));
        s1.setReviewCount(18);
        serviceListingRepository.save(s1);

        // Service 2: Combine Harvester
        if (provider2 != null) {
            ServiceListing s2 = new ServiceListing();
            s2.setProvider(provider2);
            s2.setServiceType("harvester");
            s2.setVehicleType("Track Combine Harvester (Preet 987)");
            s2.setVehicleCount(2);
            s2.setVehicleCapacity("Multi-Crop Header 14ft");
            s2.setVehicleRegistrationNo("MP-37-H-5678");
            s2.setVehicleDescription("Heavy-duty rubber-track combine harvester with straw walker. Operates smoothly in wet paddy or dry wheat fields.");
            s2.setPricePerDay(new BigDecimal("7500.00"));
            s2.setIsAvailable(true);
            s2.setIsActive(true);
            s2.setServiceStates("Madhya Pradesh");
            s2.setServiceDistricts("Sehore, Vidisha, Raisen, Hoshangabad");
            s2.setServiceRadius(100);
            s2.setRating(BigDecimal.valueOf(4.7));
            s2.setReviewCount(14);
            serviceListingRepository.save(s2);

            // Service 3: Harvesting Crew Manpower
            ServiceListing s3 = new ServiceListing();
            s3.setProvider(provider2);
            s3.setServiceType("manpower");
            s3.setManpowerAvailable(12);
            s3.setManpowerSkills("Harvest cutting, crop bundling, bagging, sorting, and loading");
            s3.setManpowerDescription("Trained and disciplined agricultural labor crew for swift crop harvesting and bag loading.");
            s3.setPricePerDay(new BigDecimal("3600.00"));
            s3.setIsAvailable(true);
            s3.setIsActive(true);
            s3.setServiceStates("Madhya Pradesh");
            s3.setServiceDistricts("Sehore, Vidisha, Bhopal");
            s3.setServiceRadius(75);
            s3.setRating(BigDecimal.valueOf(4.8));
            s3.setReviewCount(9);
            serviceListingRepository.save(s3);
        }

        logger.info("✅ Service listings seeded successfully");
    }

    private void seedHarvestRequirements() {
        if (harvestRequirementRepository.count() > 0) return;

        User farmer = userRepository.findByEmailIgnoreCase("farmer@demo.com").orElse(null);
        if (farmer == null) return;

        logger.info("📋 Seeding Harvest Requirements...");

        HarvestRequirement r1 = new HarvestRequirement();
        r1.setFarmer(farmer);
        r1.setCropType("Wheat");
        r1.setVehicleType("Truck (10 Ton)");
        r1.setManpower(4);
        r1.setQuantityAmount(new BigDecimal("150"));
        r1.setQuantityUnit("Quintal");
        r1.setDurationValue(2);
        r1.setDurationUnit("Days");
        r1.setAddress("Farm Plot 14, Sanwer Road");
        r1.setDistrict("Indore");
        r1.setState("Madhya Pradesh");
        r1.setPincode("453551");
        r1.setUrgency("normal");
        r1.setNotes("Require 10-ton truck for direct transit from field to Indore Mandi along with 4 loaders.");
        r1.setStatus("open");
        harvestRequirementRepository.save(r1);

        HarvestRequirement r2 = new HarvestRequirement();
        r2.setFarmer(farmer);
        r2.setCropType("Soybean");
        r2.setVehicleType("Harvester");
        r2.setManpower(2);
        r2.setQuantityAmount(new BigDecimal("80"));
        r2.setQuantityUnit("Quintal");
        r2.setDurationValue(1);
        r2.setDurationUnit("Days");
        r2.setAddress("Village Depalpur");
        r2.setDistrict("Indore");
        r2.setState("Madhya Pradesh");
        r2.setPincode("453115");
        r2.setUrgency("urgent");
        r2.setNotes("Combine harvester needed urgently before forecasted rain showers. Clean field access.");
        r2.setStatus("open");
        harvestRequirementRepository.save(r2);

        logger.info("✅ Harvest requirements seeded successfully");
    }

    private void seedFeedbacks() {
        if (feedbackRepository.count() > 0) return;

        User farmer = userRepository.findByEmailIgnoreCase("farmer@demo.com").orElse(null);
        User buyer = userRepository.findByEmailIgnoreCase("priya@buyer.com").orElse(null);
        User provider = userRepository.findByEmailIgnoreCase("provider@demo.com").orElse(null);

        if (farmer == null || buyer == null || provider == null) return;

        logger.info("⭐ Seeding initial Feedbacks...");

        Feedback f1 = new Feedback();
        f1.setFromUser(buyer);
        f1.setToUser(farmer);
        f1.setRating(5);
        f1.setComment("Exceptional quality Sharbati wheat! Perfectly cleaned, dry, and accurately weighed. Looking forward to our next contract.");
        f1.setTags("Quality Produce, Punctual, Honest");
        f1.setIsPublic(true);
        f1.setReferenceType("purchase");
        feedbackRepository.save(f1);

        Feedback f2 = new Feedback();
        f2.setFromUser(farmer);
        f2.setToUser(provider);
        f2.setRating(5);
        f2.setComment("Suresh Logistics arrived right on time with clean trucks and polite loaders. Zero spillage during transport.");
        f2.setTags("On-Time, Careful Handling, Fair Price");
        f2.setIsPublic(true);
        f2.setReferenceType("service");
        feedbackRepository.save(f2);

        logger.info("✅ Feedbacks seeded successfully");
    }
}
