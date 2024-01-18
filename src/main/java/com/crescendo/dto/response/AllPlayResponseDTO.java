package com.crescendo.dto.response;

import com.crescendo.entity.AllPlayList;
import com.crescendo.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties("member")
public class AllPlayResponseDTO {

    private List<AllPlayListResponseDTO> allPlayLists;
    }

