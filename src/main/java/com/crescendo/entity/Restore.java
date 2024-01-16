package com.crescendo.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
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
@Table(name = "restore")
public class Restore{
    @Id
    @Column(name = "restore_no")
    @Builder.Default
    private String restoreNo = UUID.randomUUID().toString();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "account")
    private Member member;

    @Column(name = "delete_time")
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime deleteTime = LocalDateTime.now().plusSeconds(15);
}
