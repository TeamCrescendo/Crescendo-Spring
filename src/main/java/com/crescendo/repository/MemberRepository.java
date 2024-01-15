package com.crescendo.repository;

import com.crescendo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {
    // 계정명 으로 회원 단일 조회
    @Query("SELECT m FROM Member m WHERE m.account = ?1")
    Member getOne(String account);

    // 게정명 이랑 이메일 중복검사
    @Query("SELECT COUNT(m) FROM Member m WHERE " +
            "(:type = 'account' AND m.account = :keyword) OR " +
            "(:type = 'email' AND m.email = :keyword)")
    int isDuplication(@Param("type") String type, @Param("keyword") String keyword);

}
