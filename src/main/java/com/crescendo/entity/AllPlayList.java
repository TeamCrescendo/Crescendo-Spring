package com.crescendo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "allplaylist")
public class AllPlayList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pl_id")
    private Long plId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", nullable = false)
    private Member account;

    @Column(name = "pl_name", nullable = false)
    private String plName;

    @Column(name = "pl_share")
    private boolean share;

    @CreationTimestamp
    private LocalDateTime plCreateDateTime;
}
