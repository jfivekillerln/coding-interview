package com.lexisnexis.ps.interview.coding.repo;

public interface ICreditReportRepository {
    CreditReport findByRecordId(long orgId, long recordId);
}
