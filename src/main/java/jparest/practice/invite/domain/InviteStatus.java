package jparest.practice.invite.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InviteStatus {
     WAITING, // 초대를 생성했으나, 아무 응답도 하지 않은 상태
     ACCEPT,
     REJECT, // 초대받은 유저의 거절
     CANCEL // 초대한 유저의 취소
}
