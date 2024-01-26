package com.crescendo.report.repository;

import com.crescendo.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportNo(Long reportNo);

}
