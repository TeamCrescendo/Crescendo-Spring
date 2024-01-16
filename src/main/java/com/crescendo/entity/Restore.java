package com.crescendo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Restore{
    @Id
    @Column(name = "restore_no")
    private String restoreNo = UUID.randomUUID().toString();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    private Member member;

    @Column(name = "delete_time")
    @CreationTimestamp
    private LocalDateTime deleteTime = LocalDateTime.now().plusHours(30);
}
