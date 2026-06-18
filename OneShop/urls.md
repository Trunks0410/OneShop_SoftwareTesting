# Tổng hợp Danh sách URL dự án OneShop

Tài liệu này tổng hợp toàn bộ các đường dẫn (URL) có trong hệ thống **OneShop**, được phân chia theo từng nhóm vai trò và tính năng để phục vụ việc kiểm thử, phát triển hoặc tích hợp giao diện.

---

## 🌐 1. Trang công cộng (Public & Khách vãng lai)
Các trang này có thể truy cập tự do mà không cần đăng nhập:

| Phương thức | Đường dẫn (URL) | Hàm xử lý trong Controller | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | `HomeController.index()` | Trang chủ hệ thống. |
| `GET` | `/products` | `ProductController.listProducts()` | Danh sách sản phẩm (có bộ lọc, tìm kiếm, phân trang). |
| `GET` | `/product/{productId}` | `ProductController.viewProductDetail()` | Trang chi tiết sản phẩm. |
| `GET` | `/contact` | `HomeController.contact()` | Trang liên hệ. |
| `GET` | `/news` | `HomeController.news()` | Trang tin tức. |
| `GET` | `/shipping-policy` | `HomeController.shippingPolicy()` | Trang chính sách vận chuyển. |
| `GET` | `/shop/register` | `ShopRegistrationController.showRegistrationForm()` | Trang đăng ký mở gian hàng (Vendor). |
| `POST` | `/shop/register` | `ShopRegistrationController.processRegistration()` | Gửi yêu cầu đăng ký mở gian hàng. |

---

## 🔑 2. Xác thực tài khoản (Auth)
Quản lý phiên đăng nhập và bảo mật tài khoản:

| Phương thức | Đường dẫn (URL) | Hàm xử lý trong Controller | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/login` | `AuthController.showLoginForm()` | Giao diện đăng nhập. |
| `GET` | `/register` | `AuthController.showRegisterForm()` | Giao diện đăng ký khách hàng mới. |
| `POST` | `/register` | `AuthController.registerUser()` | Gửi dữ liệu đăng ký tài khoản. |
| `GET` | `/verify-otp` | `AuthController.verifyOtpForm()` | Giao diện xác thực mã OTP đăng ký. |
| `POST` | `/verify-otp` | `AuthController.verifyOtp()` | Gửi mã OTP xác thực đăng ký. |
| `GET` | `/forgot` | `AuthController.forgotForm()` | Giao diện yêu cầu quên mật khẩu. |
| `POST` | `/forgot` | `AuthController.forgotPassword()` | Gửi yêu cầu lấy lại mật khẩu qua email. |
| `GET` | `/reset-password` | `AuthController.resetForm()` | Giao diện đặt lại mật khẩu mới. |
| `POST` | `/reset-password` | `AuthController.resetPassword()` | Gửi mật khẩu mới kèm mã OTP. |
| `POST` | `/logout` | *Spring Security* | Đăng xuất tài khoản khỏi hệ thống. |

---

## 🛒 3. Khách hàng (User / Customer)
Yêu cầu tài khoản có quyền `ROLE_USER` (Khách hàng):

| Phương thức | Đường dẫn (URL) | Hàm xử lý trong Controller | Mô tả |
| :--- | :--- | :--- | :--- |
| **Hồ sơ & Địa chỉ** | | | |
| `GET` | `/user/profile` | `UserController.showProfileForm()` | Xem trang Hồ sơ cá nhân. |
| `POST` | `/user/profile/update` | `UserController.updateProfile()` | Cập nhật thông tin Hồ sơ. |
| `GET` | `/user/addresses` | `UserController.showAddressManagement()` | Trang quản lý Sổ địa chỉ giao hàng. |
| `POST` | `/user/addresses/set-default/{id}` | `UserController.setDefaultAddress()` | Thiết lập địa chỉ mặc định. |
| `POST` | `/user/addresses/delete/{id}` | `UserController.deleteAddress()` | Xóa một địa chỉ giao hàng. |
| **Giỏ hàng & Đặt hàng** | | | |
| `GET` | `/cart` | `CartController.viewCart()` | Xem chi tiết giỏ hàng. |
| `GET` | `/cart/add/{variantId}` | `CartController.addToCartRedirect()` | Thêm sản phẩm vào giỏ (chuyển hướng). |
| `GET` | `/cart/remove/{variantId}` | `CartController.removeFromCart()` | Xóa sản phẩm khỏi giỏ (chuyển hướng). |
| `GET` | `/pay` | `CheckoutController.showCheckoutPage()` | Trang điền thông tin thanh toán (Checkout). |
| `POST` | `/placeOrder` | `OrderController.placeOrder()` | Xác nhận đặt hàng. |
| `GET` | `/order/success` | `OrderController.orderSuccessPage()` | Trang thông báo đặt hàng thành công. |
| `GET` | `/vnpay_return` | `OrderController.vnpayReturn()` | Trang nhận phản hồi thanh toán từ ví VNPay. |
| **Đơn hàng & Đánh giá** | | | |
| `GET` | `/user/orders` | `UserOrdersController.listOrders()` | Xem danh sách đơn hàng đã mua. |
| `GET` | `/user/orders/{id}/details` | `UserOrdersController.getOrderDetails()` | Xem chi tiết đơn hàng. |
| `POST` | `/user/orders/{id}/cancel` | `UserOrdersController.cancelOrder()` | Gửi yêu cầu hủy đơn hàng. |
| `GET` | `/user/orders/{orderId}/review` | `ReviewController.showReviewForm()` | Form đánh giá các sản phẩm trong đơn. |
| `POST` | `/user/orders/{orderId}/review/{productId}`| `ReviewController.submitReview()` | Gửi đánh giá sản phẩm. |

---

## 🏪 4. Kênh Nhà bán hàng (Vendor Dashboard)
Yêu cầu tài khoản có quyền `ROLE_VENDOR` (Nhà bán hàng đã được phê duyệt Shop):

| Phương thức | Đường dẫn (URL) | Hàm xử lý trong Controller | Mô tả |
| :--- | :--- | :--- | :--- |
| **Tổng quan & Hồ sơ** | | | |
| `GET` | `/vendor/dashboard` | `VendorController.dashboard()` | Trang tổng quan doanh số (Bảng điều khiển). |
| `GET` | `/vendor/profile` | `VendorController.showProfileForm()` | Xem hồ sơ nhà bán hàng. |
| `POST` | `/vendor/profile/update` | `VendorController.updateProfile()` | Cập nhật hồ sơ nhà bán hàng. |
| `GET` | `/vendor/shop` | `VendorController.shopManagement()` | Trang quản lý thông tin Shop. |
| `POST` | `/vendor/shop/update` | `VendorController.updateShop()` | Cập nhật Logo, Banner, Mô tả của Shop. |
| **Sản phẩm & Biến thể** | | | |
| `GET` | `/vendor/products` | `VendorController.productList()` | Danh sách sản phẩm của Shop. |
| `GET` | `/vendor/products/add` | `VendorController.addProductForm()` | Form thêm sản phẩm mới. |
| `GET` | `/vendor/products/edit/{id}` | `VendorController.editProductForm()` | Form chỉnh sửa sản phẩm. |
| `POST` | `/vendor/products/save` | `VendorController.saveProduct()` | Lưu (Thêm/Sửa) sản phẩm. |
| `POST` | `/vendor/products/delete/{id}` | `VendorController.deleteProduct()` | Xóa sản phẩm. |
| **Danh mục & Thương hiệu** | | | |
| `GET` | `/vendor/categories` | `VendorController.categoryManagement()`| Xem danh sách và quản lý danh mục. |
| `POST` | `/vendor/categories/add` | `VendorController.addCategory()` | Thêm danh mục mới. |
| `POST` | `/vendor/categories/delete/{id}` | `VendorController.deleteCategory()`| Xóa danh mục. |
| `GET` | `/vendor/brands` | `VendorController.brandManagement()` | Xem danh sách và quản lý thương hiệu của shop. |
| `POST` | `/vendor/brands/add` | `VendorController.addBrand()` | Thêm thương hiệu mới. |
| `POST` | `/vendor/brands/delete/{id}` | `VendorController.deleteBrand()` | Xóa thương hiệu. |
| **Quản lý Đơn hàng** | | | |
| `GET` | `/vendor/orders` | `VendorController.orderList()` | Danh sách đơn hàng mua từ Shop. |
| `GET` | `/vendor/orders/details/{id}` | `VendorController.orderDetails()` | Xem chi tiết đơn hàng của khách. |
| `POST` | `/vendor/orders/update-status` | `VendorController.updateOrderStatus()` | Cập nhật trạng thái đơn hàng. |
| `POST` | `/vendor/orders/assign-shipper` | `VendorController.assignShipperAjax()` | Gán Shipper vận chuyển (AJAX). |
| `POST` | `/vendor/orders/update-shipping` | `VendorController.updateShippingDetailsAjax()`| Cập nhật thông tin vận chuyển (AJAX). |
| `GET` | `/vendor/orders/{id}/packing-slip` | `VendorController.downloadPackingSlip()` | Tải phiếu đóng gói (PDF). |
| `GET` | `/vendor/orders/{id}/shipping-label` | `VendorController.downloadShippingLabel()` | Tải nhãn dán vận chuyển (PDF). |
| **Doanh thu & Báo cáo** | | | |
| `GET` | `/vendor/revenue` | `VendorController.revenueManagement()` | Thống kê doanh thu theo biểu đồ. |
| `GET` | `/vendor/reports/sales/download` | `VendorController.downloadSalesReport()` | Tải file Excel báo cáo doanh số. |
| `GET` | `/vendor/chat` | `ChatPageController.vendorChatList()` | Quản lý danh sách chat hỗ trợ khách hàng. |

---

## 🚚 5. Kênh Người giao hàng (Shipper Dashboard)
Yêu cầu tài khoản có quyền `ROLE_SHIPPER`:

| Phương thức | Đường dẫn (URL) | Hàm xử lý trong Controller | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/shipper/orders` | `ShipperController.listAssignedOrders()` | Danh sách đơn hàng được gán để đi giao. |
| `POST` | `/shipper/orders/{id}/update-status` | `ShipperController.updateOrderStatus()` | Cập nhật trạng thái (Đang giao, Thành công,...). |
| `GET` | `/shipper/profile` | `ShipperController.showProfileForm()` | Xem hồ sơ và Đơn vị vận chuyển liên kết. |
| `POST` | `/shipper/profile/update` | `ShipperController.updateProfile()` | Cập nhật hồ sơ shipper. |

---

## 🔑 6. Kênh Quản trị viên (Admin Dashboard)
Yêu cầu tài khoản có quyền `ROLE_ADMIN`:

| Phương thức | Đường dẫn (URL) | Hàm xử lý trong Controller | Mô tả |
| :--- | :--- | :--- | :--- |
| **Quản lý Cửa hàng** | | | |
| `GET` | `/admin/shops` | `AdminProductController.listAllShops()` | Danh sách tất cả Shop trên hệ thống. |
| `GET` | `/admin/shops/{id}/approve` | `AdminProductController.approveShop()` | Phê duyệt yêu cầu mở Shop mới. |
| `GET` | `/admin/shops/{id}/reject` | `AdminProductController.rejectShop()` | Từ chối yêu cầu mở Shop mới. |
| `GET` | `/admin/shops/{id}/deactivate` | `AdminProductController.deactivateShop()` | Khóa hoạt động của một Shop. |
| `GET` | `/admin/shops/{id}/reactivate` | `AdminProductController.reactivateShop()` | Mở khóa lại Shop. |
| `GET` | `/admin/shops/{shopId}/products` | `AdminProductController.listShopProducts()` | Xem tất cả sản phẩm thuộc về một Shop cụ thể. |
| **Quản lý Sản phẩm** | | | |
| `GET` | `/admin/products/updateStatus` | `AdminProductController.updateProductStatus()`| Duyệt hoặc từ chối sản phẩm của Shop. |
| `GET` | `/admin/products/delete/{id}` | `AdminProductController.deleteProduct()` | Xóa sản phẩm khỏi hệ thống. |
| **Quản lý Thành viên** | | | |
| `GET` | `/admin/users` | `AdminUserController.manageUsers()` | Quản lý tài khoản (User, Vendor, Shipper). |
| `POST` | `/admin/users/change-role/{userId}` | `AdminUserController.changeUserRole()` | Thay đổi vai trò (Role) của thành viên. |
| `GET` | `/admin/users/toggle-status/{userId}`| `AdminUserController.toggleUserStatus()` | Khóa/Mở khóa tài khoản thành viên. |
| `POST` | `/admin/users/create-shipper` | `AdminUserController.createShipperAccount()` | Tạo tài khoản Shipper mới gửi mail mật khẩu. |
| `GET` | `/admin/shipper/create` | `AdminShipperController.showCreateShipperForm()`| Form tạo tài khoản Shipper. |
| **Quản lý cấu hình & Hệ thống** | | | |
| `GET` | `/admin/categories` | `AdminCategoryController.listCategories()` | Quản lý toàn bộ danh mục hệ thống. |
| `POST` | `/admin/categories/save` | `AdminCategoryController.saveCategory()` | Thêm/sửa danh mục. |
| `POST` | `/admin/categories/delete` | `AdminCategoryController.performDeleteCategory()`| Xóa danh mục. |
| `GET` | `/admin/shipping_company` | `AdminShippingCompanyController.listShippingCompanies()`| Quản lý danh sách đơn vị vận chuyển. |
| `POST` | `/admin/shipping-companies/save` | `AdminShippingCompanyController.saveShippingCompany()`| Thêm đơn vị vận chuyển mới. |
| `POST` | `/admin/shipping-companies/update` | `AdminShippingCompanyController.updateShippingCompany()`| Sửa thông tin đơn vị vận chuyển. |
| `POST` | `/admin/shipping-companies/{id}/toggle-status`| `AdminShippingCompanyController.toggleStatus()`| Kích hoạt/Vô hiệu hóa đơn vị vận chuyển. |
| `GET` | `/admin/promotions` | `AdminPromotionController.listPromotions()` | Quản lý mã giảm giá toàn sàn. |
| `POST` | `/admin/promotions/save` | `AdminPromotionController.saveOrUpdatePromotion()`| Thêm/Sửa mã giảm giá. |
| `POST` | `/admin/promotions/delete/{id}` | `AdminPromotionController.deletePromotion()` | Xóa mã giảm giá. |
| `GET` | `/admin/promotion-types` | `AdminPromotionTypeController.listPromotionTypes()`| Danh sách các loại mã giảm giá. |
| `POST` | `/admin/promotion-types/add` | `AdminPromotionTypeController.addPromotionType()`| Thêm loại giảm giá mới. |
| `POST` | `/admin/promotion-types/delete/{id}`| `AdminPromotionTypeController.deletePromotionType()`| Xóa loại giảm giá. |
| `GET` | `/admin/app-discounts` | `AdminAppDiscountController.listApprovedShops()` | Quản lý cấu hình chiết khấu hệ thống. |

---

## 🔌 7. Các API Backend & Trò chuyện (AJAX / WebSocket)

| Phương thức | Đường dẫn (URL) | Hàm xử lý trong Controller | Mô tả |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/cart/add/{variantId}` | `CartController.addToCartApi()` | Thêm sản phẩm vào giỏ hàng qua AJAX. |
| `POST` | `/api/cart/remove/{variantId}` | `CartController.removeFromCartApi()` | Xóa sản phẩm khỏi giỏ hàng qua AJAX. |
| `POST` | `/api/cart/update` | `CartController.updateCartItem()` | Cập nhật số lượng sản phẩm qua AJAX. |
| `POST` | `/api/cart/apply-voucher` | `CartController.applyVoucher()` | Áp dụng mã giảm giá qua AJAX. |
| `POST` | `/api/cart/remove-voucher` | `CartController.removeVoucher()` | Hủy áp dụng mã giảm giá. |
| `GET` | `/api/addresses/{id}` | `CheckoutController.getAddressById()` | Lấy địa chỉ giao hàng theo ID qua AJAX. |
| `GET` | `/chat/{targetId}` | `ChatPageController.openChat()` | Mở phòng chat giữa 2 tài khoản. |
| `GET` | `/api/chat/users` | `ChatApiController.getChatUsers()` | Lấy danh sách liên hệ chat. |
| `GET` | `/api/chat/history` | `ChatHistoryController.getHistory()` | Lấy lịch sử hội thoại chat. |
| `POST` | `/api/addresses` | `AddressController.createAddress()` | Thêm địa chỉ mới qua API. |
| `PUT` | `/api/addresses/{id}` | `AddressController.updateAddress()` | Sửa địa chỉ giao hàng qua API. |
