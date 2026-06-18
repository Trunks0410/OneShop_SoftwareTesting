package com.oneshop.entity;

/**
 * Enum định nghĩa các trạng thái của đơn hàng.
 * Tên file: OrderStatus.java
 */
public enum OrderStatus {
    PENDING("Chờ xử lý"),        // Chờ thanh toán (đơn hàng vừa tạo, chờ VNPAY/COD)
    CONFIRMED("Đã xác nhận"),      // Đã xác nhận (Shop đã thấy đơn, đang chuẩn bị hàng)
    DELIVERING("Đang giao"),        // Đang giao (Đã bàn giao cho shipper)
    DELIVERED("Đã giao"),      // Đã giao thành công
    CANCELLED("Đã hủy"),      // Đã hủy (bởi khách hoặc shop)
    RETURNED("Trả hàng/Hoàn tiền");        // Trả hàng

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
