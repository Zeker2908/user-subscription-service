package com.example.userservice.model;

public enum SubscriptionStatus {
    ACTIVE,         // Подписка активна и оплачена
    PAUSED,         // Подписка приостановлена (например, по запросу пользователя)
    CANCELED,       // Подписка отменена
    EXPIRED,        // Подписка истекла (можно автоматически выставлять)
    PAYMENT_PENDING // Ожидается оплата
}
