package com.crescendo.restore.entity;

import com.crescendo.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "restore_no")
    private String restoreNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    private Member member;

    @Column(name = "delete_time", updatable = false)
    //@CreationTimestamp
    @Builder.Default
    private LocalDateTime deleteTime = LocalDateTime.now().plusMinutes(10);
}
