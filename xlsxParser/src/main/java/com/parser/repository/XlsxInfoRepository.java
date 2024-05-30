package com.parser.repository;

import com.parser.model.XlsxInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface XlsxInfoRepository extends JpaRepository<XlsxInfo, Integer> {


}
