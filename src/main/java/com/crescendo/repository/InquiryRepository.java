package com.crescendo.repository;

import com.crescendo.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, String> {
    @Query("SELECT i.inquiryId FROM Inquiry i WHERE i.member.account = ?1")
    List<String> getAllByAccount(String account);
}
