package com.crescendo.likeAndDislike.repository;

import com.crescendo.board.entity.Board;
import com.crescendo.board.entity.Like;
import com.crescendo.likeAndDislike.entity.LikeAndDislike;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface LikeAndDislikeRepository extends JpaRepository<LikeAndDislike, Long> {
        LikeAndDislike findByMemberAccountAndBoard_BoardNo(String account, Long boardNo);

        void deleteByBoard(Board board);
}
