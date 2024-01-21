package com.crescendo.score.entity;

import com.crescendo.member.entity.Member;
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
    @Builder.Default
    private String scoreImageUrl = "";

    @CreationTimestamp
    @Column(name = "score_upload_date_time")
    private LocalDateTime scoreUploadDateTime;

    @Column(name = "score_title", nullable = false)
    private String scoreTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "score_genre")
    private GENRE scoreGenre;


    public enum GENRE{
        VALUE1("JAZZ"),
        VALUE2("HIPHOP"),
        VALUE3("POP"),
        VALUE4("CLASSIC"),
        VALUE5("BALLADE"),
        VALUE6("JADANCEZZ"),
        VALUE7("DISCO"),
        VALUE8("HOUSE"),
        VALUE9("ROCK"),
        VALUE10("ELECTRONIC"),
        VALUE11("TROT"),
        VALUE12("OTHER");

        private final String stringValue;

        GENRE(String stringValue){
            this.stringValue = stringValue;
        }

        public String getStringValue(){
            return stringValue;
        }
    }



}
