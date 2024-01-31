package com.crescendo.member.repository;

import com.crescendo.member.entity.Member;
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
            "(:type = 'email' AND m.email = :keyword) OR " +
            "(:type = 'nick_name' AND m.userName = :keyword)")
    int isDuplication(@Param("type") String type, @Param("keyword") String keyword);

    boolean existsByAccount(String account);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    // 개인멤버  서비스 이용횟수 반환
    @Query("SELECT m.userDownloadChance FROM Member m WHERE m.account = ?1")
    int downCountUserDownloadChanceByAccount(String account);

    Member findMemberByEmail(String email);
}
