package com.crescendo.allPlayList.entity;

import com.crescendo.member.entity.Member;
import com.crescendo.playList.entity.PlayList;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(exclude = "plId")
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
    private boolean plShare;

    @CreationTimestamp
    private LocalDateTime plCreateDateTime;

<<<<<<< HEAD
    @Column(name ="score_count")
    private Integer scoreCount;
=======
    @OneToMany(mappedBy = "plId",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PlayList> playLists=new ArrayList<>();

>>>>>>> seon/01/30
}
