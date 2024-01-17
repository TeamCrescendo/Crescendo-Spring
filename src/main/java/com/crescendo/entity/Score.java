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
@Table(name = "score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_no")
    private int scoreNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    private Member member;

    @Column(name = "score_img_url")
    private String scoreImageUrl;

    @CreationTimestamp
    @Column(name = "score_upload_date_time")
    private LocalDateTime scoreUploadDateTime;

    @Column(name = "score_title", nullable = false)
    private String scoreTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "score_genre")
    private GENRE scoreGenre;


    public enum GENRE{
        JAZZ, HIPHOP, POP, CLASSIC, BALLADE, DANCE, DISCO, HOUSE, ROCK, ELECTRONIC, TROT
    }



}
