package com.crescendo.inquiry.entity;

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
@Table(name = "inquiry")
public class Inquiry {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "inquiry_id")
    private String inquiryId;

    @Column(nullable = false, name = "inquiry_title")
    private String inquiryTitle;

    @Column(nullable = false, name = "inquiry_content")
    private String inquiryContent;

    @CreationTimestamp
    @Column(name = "inquiry_date_time", updatable = false)
    private LocalDateTime inquiryDateTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account")
    private Member member;


}
