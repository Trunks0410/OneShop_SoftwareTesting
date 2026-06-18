# Danh sách Tài khoản Mẫu Hệ thống OneShop

Dưới đây là danh sách các tài khoản mẫu đã được tự động thêm vào cơ sở dữ liệu để phục vụ việc kiểm thử các chức năng theo từng vai trò (role) khác nhau trong hệ thống.

---

## 1. Bảng tổng hợp tài khoản

| Vai trò (Role) | Tên đăng nhập (Username) | Mật khẩu (Password) | Email | Tên hiển thị (Full Name) |
| :--- | :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `123456` | `admin@oneshop.com` | Quản Trị Viên |
| **USER** (Khách hàng) | `user` | `123456` | `user@oneshop.com` | Khách Hàng Mẫu |
| **VENDOR** (Nhà bán hàng) | `vendor` | `123456` | `vendor@oneshop.com` | Nhà Bán Hàng Mỹ Phẩm |
| **SHIPPER** (Vận chuyển) | `shipper` | `123456` | `shipper@oneshop.com` | Người Vận Chuyển |

---

## 2. Thông tin chi tiết từng tài khoản

### 🔑 Quản trị viên (ADMIN)
- **Username:** `admin`
- **Password:** `123456`
- **Email:** `admin@oneshop.com`
- **Quyền hạn:** Quản lý toàn bộ hệ thống, duyệt yêu cầu mở Shop của Vendor, duyệt sản phẩm của Vendor, quản lý người dùng, shipper và các cài đặt chung.

### 🛍️ Khách hàng (USER)
- **Username:** `user`
- **Password:** `123456`
- **Email:** `user@oneshop.com`
- **Quyền hạn:** Xem sản phẩm, thêm vào giỏ hàng, áp dụng mã giảm giá, đặt hàng, thanh toán và gửi đánh giá sản phẩm.

### 🏪 Nhà bán hàng (VENDOR)
- **Username:** `vendor`
- **Password:** `123456`
- **Email:** `vendor@oneshop.com`
- **Cửa hàng sở hữu:** `OneShop Beauty`
- **Quyền hạn:** Quản lý cửa hàng của mình, đăng bán sản phẩm, quản lý kho hàng/biến thể, quản lý đơn hàng của shop và tạo các chương trình khuyến mãi riêng.

### 🚚 Người giao hàng (SHIPPER)
- **Username:** `shipper`
- **Password:** `123456`
- **Email:** `shipper@oneshop.com`
- **Quyền hạn:** Tiếp nhận đơn hàng được phân phối, cập nhật trạng thái vận chuyển (đang giao, đã giao, hủy đơn, v.v.).

---

> [!IMPORTANT]
> - Tất cả mật khẩu đã được mã hóa tự động bằng thuật toán **BCrypt** trước khi lưu vào cột `password` trong bảng `USERS`.
> - Các tài khoản này đã được kích hoạt mặc định (`activated = true`) nên có thể đăng nhập trực tiếp mà không cần qua bước xác thực OTP.
