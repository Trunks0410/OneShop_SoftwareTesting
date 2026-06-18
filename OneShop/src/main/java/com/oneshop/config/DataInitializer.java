package com.oneshop.config;

import com.oneshop.entity.*;
import com.oneshop.entity.Role.RoleName;
import com.oneshop.enums.ProductStatus;
import com.oneshop.enums.ShopStatus;
import com.oneshop.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    private static class CosmeticTemplate {
        String name;
        String categoryName;
        String brandName;
        String description;
        double price;
        double originalPrice;
        String imageUrl;
        String tags;

        CosmeticTemplate(String name, String categoryName, String brandName, String description, double price, double originalPrice, String imageUrl, String tags) {
            this.name = name;
            this.categoryName = categoryName;
            this.brandName = brandName;
            this.description = description;
            this.price = price;
            this.originalPrice = originalPrice;
            this.imageUrl = imageUrl;
            this.tags = tags;
        }
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization...");

        // 1. Initialize Roles
        initializeRoles();

        // 2. Initialize Vendor User
        User vendorUser = initializeVendorUser();

        // 2.5 Initialize Other Users (Admin, User, Shipper)
        initializeOtherUsers();

        // 3. Initialize Shop
        Shop shop = initializeShop(vendorUser);

        // 4. Initialize Brands
        initializeBrands();

        // 5. Initialize Categories
        initializeCategories();

        // 6. Initialize Products
        initializeProducts(shop);

        logger.info("Data initialization complete.");
    }

    private void initializeRoles() {
        logger.info("Initializing roles...");
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role newRole = new Role(roleName);
                roleRepository.save(newRole);
                logger.info("Created role: {}", roleName);
            }
        }
    }

    private User initializeVendorUser() {
        logger.info("Checking vendor user...");
        return userRepository.findByUsername("vendor").orElseGet(() -> {
            User vendor = new User();
            vendor.setUsername("vendor");
            vendor.setPassword(passwordEncoder.encode("123456"));
            vendor.setEmail("vendor@oneshop.com");
            vendor.setFullName("Nhà Bán Hàng Mỹ Phẩm");
            vendor.setPhoneNumber("0987654321");
            vendor.setAddress("Hà Nội, Việt Nam");
            vendor.setActivated(true);

            Role vendorRole = roleRepository.findByName(RoleName.VENDOR)
                    .orElseThrow(() -> new RuntimeException("Role VENDOR not found"));
            vendor.setRole(vendorRole);

            User saved = userRepository.save(vendor);
            logger.info("Created default vendor user (username: vendor, password: 123456)");
            return saved;
        });
    }

    private void initializeOtherUsers() {
        logger.info("Initializing other default users...");

        // Admin User
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setEmail("admin@oneshop.com");
            admin.setFullName("Quản Trị Viên");
            admin.setPhoneNumber("0912345678");
            admin.setAddress("Hệ Thống OneShop");
            admin.setActivated(true);

            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
            admin.setRole(adminRole);
            userRepository.save(admin);
            logger.info("Created default admin user (username: admin, password: 123456)");
        }

        // Customer (User)
        if (userRepository.findByUsername("user").isEmpty()) {
            User customer = new User();
            customer.setUsername("user");
            customer.setPassword(passwordEncoder.encode("123456"));
            customer.setEmail("user@oneshop.com");
            customer.setFullName("Khách Hàng Mẫu");
            customer.setPhoneNumber("0922345678");
            customer.setAddress("Hà Nội, Việt Nam");
            customer.setActivated(true);

            Role userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
            customer.setRole(userRole);
            userRepository.save(customer);
            logger.info("Created default customer user (username: user, password: 123456)");
        }

        // Shipper User
        if (userRepository.findByUsername("shipper").isEmpty()) {
            User shipper = new User();
            shipper.setUsername("shipper");
            shipper.setPassword(passwordEncoder.encode("123456"));
            shipper.setEmail("shipper@oneshop.com");
            shipper.setFullName("Người Vận Chuyển");
            shipper.setPhoneNumber("0932345678");
            shipper.setAddress("Bưu cục OneShop");
            shipper.setActivated(true);

            Role shipperRole = roleRepository.findByName(RoleName.SHIPPER)
                    .orElseThrow(() -> new RuntimeException("Role SHIPPER not found"));
            shipper.setRole(shipperRole);
            userRepository.save(shipper);
            logger.info("Created default shipper user (username: shipper, password: 123456)");
        }
    }

    private Shop initializeShop(User vendorUser) {
        logger.info("Checking shop for vendor...");
        return shopRepository.findByUserId(vendorUser.getId()).orElseGet(() -> {
            Shop shop = new Shop();
            shop.setName("OneShop Beauty");
            shop.setDescription("Cửa hàng mỹ phẩm chính hãng chất lượng cao.");
            shop.setLogo("logo.png");
            shop.setBanner("banner.jpg");
            shop.setContactEmail("vendor@oneshop.com");
            shop.setContactPhone("0987654321");
            shop.setStatus(ShopStatus.APPROVED);
            shop.setUser(vendorUser);
            shop.setCommissionRate(new BigDecimal("0.05"));

            Shop saved = shopRepository.save(shop);
            logger.info("Created default shop 'OneShop Beauty' for vendor");
            return saved;
        });
    }

    private void initializeBrands() {
        logger.info("Initializing brands...");
        String[] brandNames = {"L'Oreal", "Innisfree", "La Roche-Posay", "Anessa", "Laneige", "Maybelline", "Estee Lauder", "Simple"};
        for (String name : brandNames) {
            if (brandRepository.findByNameIgnoreCase(name).isEmpty()) {
                Brand brand = new Brand();
                brand.setName(name);
                brand.setLogoUrl("logo.png");
                brandRepository.save(brand);
                logger.info("Created brand: {}", name);
            }
        }
    }

    private void initializeCategories() {
        logger.info("Initializing categories...");
        String[] categories = {"Son môi", "Sữa rửa mặt", "Kem chống nắng", "Kem dưỡng da", "Toner & Lotion", "Tẩy trang", "Mặt nạ dưỡng da", "Serum & Tinh chất"};
        for (String name : categories) {
            if (categoryRepository.findByNameIgnoreCase(name).isEmpty()) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
                logger.info("Created category: {}", name);
            }
        }
    }

    private void initializeProducts(Shop shop) {
        long productCount = productRepository.count();
        logger.info("Current product count: {}", productCount);
        if (productCount >= 50) {
            logger.info("Products already seeded (count >= 50). Skipping seeding.");
            return;
        }

        logger.info("Seeding 50 cosmetic products...");

        List<CosmeticTemplate> templates = new ArrayList<>();
        // Son môi
        templates.add(new CosmeticTemplate("Son Kem Lì L'Oreal Paris Rouge Signature", "Son môi", "L'Oreal", "Son kem lì siêu nhẹ môi, bám màu lâu trôi.", 258000, 310000, "lipstick.png", "son moi, son kem, loreal"));
        templates.add(new CosmeticTemplate("Son Thỏi Innisfree Airy Matte Lipstick", "Son môi", "Innisfree", "Son thỏi lì dạng matte siêu mịn, giữ ẩm tốt.", 320000, 380000, "lipstick.png", "son moi, son thoi, innisfree"));
        templates.add(new CosmeticTemplate("Son Kem Lì Maybelline Superstay Matte Ink", "Son môi", "Maybelline", "Son kem lì giữ màu đến 16 tiếng, không trôi không lem.", 205000, 240000, "lipstick.png", "son moi, son kem, maybelline"));
        templates.add(new CosmeticTemplate("Son Dưỡng Estee Lauder Pure Color Envy", "Son môi", "Estee Lauder", "Son dưỡng môi cao cấp cấp ẩm sâu và tạo sắc hồng tự nhiên.", 850000, 950000, "lipstick.png", "son moi, son duong, estee lauder"));
        templates.add(new CosmeticTemplate("Son Tint Bóng Laneige Layering Lip Bar", "Son môi", "Laneige", "Son tint bóng phân tầng màu độc đáo căng mọng môi.", 450000, 520000, "lipstick.png", "son moi, son tint, laneige"));
        templates.add(new CosmeticTemplate("Son Dưỡng Có Màu L'Oreal Glow Paradise", "Son môi", "L'Oreal", "Son dưỡng căng mọng môi với chiết xuất thiên nhiên.", 269000, 320000, "lipstick.png", "son moi, son duong, loreal"));
        templates.add(new CosmeticTemplate("Son Kem Maybelline Sensational Liquid Matte", "Son môi", "Maybelline", "Son kem mịn mượt mờ môi chuẩn sắc.", 185000, 220000, "lipstick.png", "son moi, son kem, maybelline"));

        // Sữa rửa mặt
        templates.add(new CosmeticTemplate("Sữa rửa mặt Innisfree Green Tea Amino Cleansing Foam", "Sữa rửa mặt", "Innisfree", "Sữa rửa mặt chiết xuất trà xanh dưỡng ẩm sâu.", 220000, 260000, "cleanser.png", "sua rua mat, tra xanh, innisfree"));
        templates.add(new CosmeticTemplate("Sữa Rửa Mặt La Roche-Posay Effaclar Gel", "Sữa rửa mặt", "La Roche-Posay", "Sữa rửa mặt dạng gel cho da dầu mụn nhạy cảm.", 385000, 450000, "cleanser.png", "sua rua mat, ngua mun, la roche posay"));
        templates.add(new CosmeticTemplate("Sữa Rửa Mặt Simple Refreshing Facial Wash Gel", "Sữa rửa mặt", "Simple", "Gel rửa mặt dịu nhẹ lành tính cho mọi loại da.", 110000, 140000, "cleanser.png", "sua rua mat, diu nhe, simple"));
        templates.add(new CosmeticTemplate("Sữa Rửa Mặt L'Oreal Glycolic Bright Daily Foam", "Sữa rửa mặt", "L'Oreal", "Sữa rửa mặt làm sáng da mờ thâm nám.", 145000, 180000, "cleanser.png", "sua rua mat, sang da, loreal"));
        templates.add(new CosmeticTemplate("Sữa rửa mặt Innisfree Bija Trouble Facial Foam", "Sữa rửa mặt", "Innisfree", "Sữa rửa mặt cho da mụn chiết xuất quả Bija.", 210000, 250000, "cleanser.png", "sua rua mat, da mun, innisfree"));
        templates.add(new CosmeticTemplate("Sữa Rửa Mặt La Roche-Posay Toleriane Dermo", "Sữa rửa mặt", "La Roche-Posay", "Sữa rửa mặt kiêm tẩy trang dịu nhẹ cho da kích ứng.", 420000, 490000, "cleanser.png", "sua rua mat, da nhay cam, la roche posay"));
        templates.add(new CosmeticTemplate("Sữa Rửa Mặt Cấp Ẩm Simple Moisturising Wash", "Sữa rửa mặt", "Simple", "Sữa rửa mặt cấp ẩm nuôi dưỡng làn da mịn màng.", 115000, 150000, "cleanser.png", "sua rua mat, cap am, simple"));

        // Kem chống nắng
        templates.add(new CosmeticTemplate("Kem Chống Nắng Anessa Perfect UV Milk", "Kem chống nắng", "Anessa", "Sữa chống nắng kiềm dầu bảo vệ da tối đa.", 575000, 685000, "sunscreen.png", "kem chong nang, sua chong nang, anessa"));
        templates.add(new CosmeticTemplate("Kem Chống Nắng La Roche-Posay UVmune Fluid", "Kem chống nắng", "La Roche-Posay", "Kem chống nắng dạng sữa lỏng nhẹ chống tia UV tối ưu.", 415000, 495000, "sunscreen.png", "kem chong nang, bao ve da, la roche posay"));
        templates.add(new CosmeticTemplate("Kem Chống Nắng L'Oreal UV Defender Serum", "Kem chống nắng", "L'Oreal", "Kem chống nắng bảo vệ da và dưỡng ẩm tối ưu với HA.", 239000, 299000, "sunscreen.png", "kem chong nang, duong am, loreal"));
        templates.add(new CosmeticTemplate("Kem Chống Nắng Innisfree Intensive Triple-shield", "Kem chống nắng", "Innisfree", "Kem chống nắng 3 tác động làm sáng, chống nhăn, chống nước.", 290000, 360000, "sunscreen.png", "kem chong nang, chong nuoc, innisfree"));
        templates.add(new CosmeticTemplate("Gel Chống Nắng Anessa Moisture Mild Gel", "Kem chống nắng", "Anessa", "Gel chống nắng dưỡng ẩm dịu nhẹ cho da nhạy cảm.", 485000, 560000, "sunscreen.png", "kem chong nang, da nhay cam, anessa"));
        templates.add(new CosmeticTemplate("Kem Chống Nắng Kiềm Dầu La Roche-Posay Gel-Cream", "Kem chống nắng", "La Roche-Posay", "Kem chống nắng kiểm soát dầu hiệu quả không bóng nhờn.", 425000, 510000, "sunscreen.png", "kem chong nang, kiem dau, la roche posay"));
        templates.add(new CosmeticTemplate("Sữa Chống Nắng Dịu Nhẹ Anessa Mild Milk", "Kem chống nắng", "Anessa", "Sữa chống nắng bảo vệ da dịu nhẹ cho da cực kỳ mẫn cảm.", 595000, 710000, "sunscreen.png", "kem chong nang, sua chong nang, anessa"));

        // Kem dưỡng da
        templates.add(new CosmeticTemplate("Kem Dưỡng Ẩm Laneige Water Bank Cream", "Kem dưỡng da", "Laneige", "Kem dưỡng ẩm sâu thế hệ mới cho da khô và da dầu.", 750000, 890000, "cream.png", "kem duong da, kem duong am, laneige"));
        templates.add(new CosmeticTemplate("Kem Dưỡng Phục Hồi B5 La Roche-Posay Baume", "Kem dưỡng da", "La Roche-Posay", "Kem dưỡng phục hồi da kích ứng tổn thương Cicaplast Baume B5+.", 325000, 390000, "cream.png", "kem duong da, phuc hoi da, la roche posay"));
        templates.add(new CosmeticTemplate("Kem Dưỡng Trắng L'Oreal Glycolic Bright Day", "Kem dưỡng da", "L'Oreal", "Kem dưỡng ngày dưỡng trắng mờ thâm với chỉ số SPF 17.", 279000, 349000, "cream.png", "kem duong da, duong trang, loreal"));
        templates.add(new CosmeticTemplate("Kem Dưỡng Trẻ Hóa Estee Lauder Supreme+ Soft", "Kem dưỡng da", "Estee Lauder", "Kem dưỡng chống lão hóa cao cấp giúp săn chắc da.", 2150000, 2450000, "cream.png", "kem duong da, chong lao hoa, estee lauder"));
        templates.add(new CosmeticTemplate("Kem Dưỡng Dịu Nhẹ Simple Hydrating Light", "Kem dưỡng da", "Simple", "Kem dưỡng ẩm ban ngày dịu nhẹ thẩm thấu nhanh.", 135000, 175000, "cream.png", "kem duong da, diu nhe, simple"));
        templates.add(new CosmeticTemplate("Kem Dưỡng Innisfree Cherry Blossom Jelly", "Kem dưỡng da", "Innisfree", "Kem dưỡng dạng gel thạch sáng da chiết xuất hoa anh đào.", 380000, 460000, "cream.png", "kem duong da, sang da, innisfree"));
        templates.add(new CosmeticTemplate("Mặt Nạ Ngủ Cấp Ẩm Laneige Water Sleeping Mask", "Kem dưỡng da", "Laneige", "Mặt nạ ngủ huyền thoại nuôi dưỡng da căng mọng qua đêm.", 650000, 780000, "cream.png", "mat na ngu, cap am, laneige"));

        // Toner & Lotion
        templates.add(new CosmeticTemplate("Nước Hoa Hồng Simple Soothing Toner", "Toner & Lotion", "Simple", "Nước hoa hồng làm dịu da cân bằng độ pH lành tính.", 120000, 160000, "toner.png", "toner, nuoc hoa hong, simple"));
        templates.add(new CosmeticTemplate("Nước Hoa Hồng Innisfree Green Tea Skin", "Toner & Lotion", "Innisfree", "Nước hoa hồng trà xanh cấp ẩm thanh lọc làn da.", 340000, 410000, "toner.png", "toner, nuoc hoa hong, innisfree"));
        templates.add(new CosmeticTemplate("Lotion Se Khít Lỗ Chân Lông La Roche-Posay", "Toner & Lotion", "La Roche-Posay", "Nước hoa hồng kiêm tẩy tế bào chết nhẹ thu nhỏ lỗ chân lông.", 395000, 480000, "toner.png", "toner, se khit lo chan long, la roche posay"));
        templates.add(new CosmeticTemplate("Tinh Chất Nước Thần L'Oreal Crystal Essence", "Toner & Lotion", "L'Oreal", "Dưỡng chất vi dòng thẩm thấu sâu làm căng mướt da.", 319000, 399000, "toner.png", "nuoc than, sang da, loreal"));
        templates.add(new CosmeticTemplate("Nước Hoa Hồng Dưỡng Ẩm Laneige Cream Skin", "Toner & Lotion", "Laneige", "Toner kết hợp cream dưỡng ẩm chuyên sâu độc đáo.", 590000, 690000, "toner.png", "toner, duong am, laneige"));
        templates.add(new CosmeticTemplate("Sữa Dưỡng Ẩm Simple Kind to Skin Rich", "Toner & Lotion", "Simple", "Sữa dưỡng ẩm chuyên sâu cho da khô nhạy cảm.", 145000, 195000, "toner.png", "lotion, sua duong, simple"));

        // Tẩy trang
        templates.add(new CosmeticTemplate("Nước Tẩy Trang L'Oreal Micellar Water 3-in-1", "Tẩy trang", "L'Oreal", "Nước tẩy trang làm sạch dịu nhẹ không cần rửa lại.", 169000, 219000, "remover.png", "tay trang, nuoc tay trang, loreal"));
        templates.add(new CosmeticTemplate("Nước Tẩy Trang La Roche-Posay Ultra", "Tẩy trang", "La Roche-Posay", "Nước tẩy trang làm sạch sâu bụi mịn cho da mẫn cảm.", 395000, 470000, "remover.png", "tay trang, da nhay cam, la roche posay"));
        templates.add(new CosmeticTemplate("Nước Tẩy Trang Simple Micellar Water", "Tẩy trang", "Simple", "Nước tẩy trang cấp ẩm dịu nhẹ sạch sâu lành tính.", 135000, 185000, "remover.png", "tay trang, diu nhe, simple"));
        templates.add(new CosmeticTemplate("Tẩy Trang Mắt Môi Maybelline Eye Lip", "Tẩy trang", "Maybelline", "Tẩy trang chuyên dụng cho mắt môi mascara son lì.", 125000, 160000, "remover.png", "tay trang, mat moi, maybelline"));
        templates.add(new CosmeticTemplate("Dầu Tẩy Trang Innisfree Green Tea", "Tẩy trang", "Innisfree", "Dầu tẩy trang trà xanh nhũ hóa cực sạch mụn đầu đen.", 390000, 460000, "remover.png", "dau tay trang, sach sau, innisfree"));
        templates.add(new CosmeticTemplate("Nước Tẩy Trang Bioderma Sensibio H2O", "Tẩy trang", "Simple", "Nước tẩy trang quốc dân dịu nhẹ làm sạch sâu bụi bẩn.", 395000, 485000, "remover.png", "tay trang, bioderma, diu nhe"));

        // Mặt nạ
        templates.add(new CosmeticTemplate("Mặt Nạ Đất Sét Innisfree Volcanic 2X", "Mặt nạ dưỡng da", "Innisfree", "Mặt nạ đất sét hút sạch bã nhờn se khít lỗ chân lông.", 320000, 380000, "mask.png", "mat na, dat set, innisfree"));
        templates.add(new CosmeticTemplate("Mặt Nạ Giấy L'Oreal Instant Glowing Mask", "Mặt nạ dưỡng da", "L'Oreal", "Mặt nạ giấy dưỡng sáng da tức thì chứa tinh chất đậm đặc.", 35000, 45000, "mask.png", "mat na giay, duong sang, loreal"));
        templates.add(new CosmeticTemplate("Mặt Nạ Giấy Cấp Ẩm Laneige Quick Mask", "Mặt nạ dưỡng da", "Laneige", "Mặt nạ giấy cấp nước tức thì làm dịu da khô ráp.", 45000, 55000, "mask.png", "mat na giay, cap nuoc, laneige"));
        templates.add(new CosmeticTemplate("Mặt Nạ Giấy Trị Mụn La Roche-Posay Mask", "Mặt nạ dưỡng da", "La Roche-Posay", "Mặt nạ làm dịu nốt mụn sưng đỏ kiềm dầu hiệu quả.", 65000, 80000, "mask.png", "mat na giay, giam mun, la roche posay"));
        templates.add(new CosmeticTemplate("Mặt Nạ Giấy Dịu Da Simple De-Stress Mask", "Mặt nạ dưỡng da", "Simple", "Mặt nạ giấy cấp ẩm thư giãn làn da mệt mỏi nhạy cảm.", 30000, 42000, "mask.png", "mat na giay, thu gian, simple"));

        // Serum & Tinh chất
        templates.add(new CosmeticTemplate("Serum Cấp Ẩm L'Oreal Hyaluronic Acid 1.5%", "Serum & Tinh chất", "L'Oreal", "Tinh chất cấp ẩm sâu cho da căng mướt mịn màng.", 399000, 499000, "serum.png", "serum, cap am, ha, loreal"));
        templates.add(new CosmeticTemplate("Tinh Chất Phục Hồi Estee Lauder Night Repair", "Serum & Tinh chất", "Estee Lauder", "Tinh chất phục hồi da chống lão hóa ban đêm đỉnh cao.", 2250000, 2600000, "serum.png", "serum, phuc hoi, chong lao hoa, estee lauder"));
        templates.add(new CosmeticTemplate("Serum Dưỡng Sáng Innisfree Green Tea Seed", "Serum & Tinh chất", "Innisfree", "Serum trà xanh cấp ẩm sâu tăng cường hàng rào bảo vệ da.", 520000, 620000, "serum.png", "serum, tra xanh, innisfree"));
        templates.add(new CosmeticTemplate("Serum Giảm Mụn Mờ Thâm La Roche-Posay Effaclar", "Serum & Tinh chất", "La Roche-Posay", "Serum trị mụn ẩn mụn đầu đen thu nhỏ lỗ chân lông.", 795000, 950000, "serum.png", "serum, tri mun, la roche posay"));
        templates.add(new CosmeticTemplate("Serum Dưỡng Sáng Da Laneige Radian-C Spot", "Serum & Tinh chất", "Laneige", "Tinh chất làm mờ vết thâm sạm đốm nâu cải thiện tông da.", 720000, 850000, "serum.png", "serum, vitamin c, laneige"));
        templates.add(new CosmeticTemplate("Tinh Chất Vitamin B3 Simple Booster", "Serum & Tinh chất", "Simple", "Serum hỗ trợ kiềm dầu giảm kích ứng dưỡng khỏe da.", 245000, 310000, "serum.png", "serum, vitamin b3, simple"));


        long startTime = System.currentTimeMillis();
        int index = 1;
        for (CosmeticTemplate t : templates) {
            Category category = categoryRepository.findByNameIgnoreCase(t.categoryName)
                    .orElseGet(() -> categoryRepository.findAll().get(0));

            Brand brand = brandRepository.findByNameIgnoreCase(t.brandName)
                    .orElseGet(() -> brandRepository.findAll().get(0));

            Product product = new Product();
            product.setName(t.name);
            product.setDescription(t.description);
            product.setBrand(brand);
            product.setCategory(category);
            product.setShop(shop);
            product.setPublished(true);
            product.setStatus(ProductStatus.SELLING);
            product.setTags(t.tags);
            product.setSalesCount((int) (Math.random() * 50));
            product.setPrice(BigDecimal.ZERO);

            // Save product to get ID
            Product savedProduct = productRepository.save(product);

            // Create primary variant
            ProductVariant variant = new ProductVariant();
            variant.setProduct(savedProduct);
            variant.setName("Mặc định");
            variant.setSku("SKU-" + index + "-" + startTime);
            variant.setPrice(BigDecimal.valueOf(t.price));
            variant.setOriginalPrice(BigDecimal.valueOf(t.originalPrice));
            variant.setStock((int) (20 + Math.random() * 100));
            variant.setActive(true);
            productVariantRepository.save(variant);

            // Create product image
            ProductImage img = new ProductImage();
            img.setProduct(savedProduct);
            img.setImageUrl(t.imageUrl);
            img.setIsPrimary(true);
            productImageRepository.save(img);

            // Update Product with variants and images references and sync prices/stock
            savedProduct.getVariants().add(variant);
            savedProduct.getImages().add(img);
            savedProduct.updatePriceFromVariants();
            savedProduct.updateStockFromVariants();
            productRepository.save(savedProduct);

            logger.info("Seeded product {}: {}", index, t.name);
            index++;
        }
        logger.info("Successfully seeded {} products.", index - 1);
    }
}