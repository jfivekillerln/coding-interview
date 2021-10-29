package com.lexisnexis.ps.interview.coding.repo;

import com.lexisnexis.ps.interview.coding.model.Lien;

public interface ILeinReportRepository {
    Lien findByLeinId(long orgId, long recordId);
}
