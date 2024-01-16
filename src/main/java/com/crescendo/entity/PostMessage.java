package com.crescendo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post_message")
public class PostMessage {
    @Id
    @Builder.Default
    private String postMessageId = UUID.randomUUID().toString();

    // 쓴 사람
    @ManyToOne
    @JoinColumn(name = "account")
    private Member member;

    // 받는 사람
    @Column(name = "post_message_receiver", nullable = false)
    private String postMessageReceiver;

    // 쪽지 내용
    @Column(name = "post_message_content", nullable = false)
    private String postMessageContent;

    // 수신자가 받았는지 여부
    @Column(name = "post_message_checked")
    @Builder.Default
    private boolean checked = false;

}
