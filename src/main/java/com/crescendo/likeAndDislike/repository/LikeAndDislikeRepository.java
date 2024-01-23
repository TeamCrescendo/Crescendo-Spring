package com.crescendo.likeAndDislike.repository;

import com.crescendo.likeAndDislike.entity.LikeAndDislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeAndDislikeRepository extends JpaRepository<LikeAndDislike, Long> {
        LikeAndDislike findByMemberAccountAndBoard_BoardNo(String account, Long boardNo);

}
