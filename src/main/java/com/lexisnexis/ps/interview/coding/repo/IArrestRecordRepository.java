package com.lexisnexis.ps.interview.coding.repo;

import com.lexisnexis.ps.interview.coding.model.ArrestRecord;

public interface IArrestRecordRepository {
    ArrestRecord findByArrestId(long recordId);
}
