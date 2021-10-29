package com.lexisnexis.ps.interview.coding.repo;

import com.lexisnexis.ps.interview.coding.model.DriverRecord;

public interface IDriverReportRepository {
    DriverRecord findByRecordId(long recordId);
}
