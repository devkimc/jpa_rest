package jparest.practice.subscription.domain;

public enum SubscriptionStatus {
    WAITING, // 가입 신청을 요청했으나, 응답이 오지 않은 상태
    ACCEPT, // 가입 신청 승인
    REJECT, // 가입 신청 거절
    CANCEL // 가입 신청한 유저의 취소
}
