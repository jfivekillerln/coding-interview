package com.lexisnexis.ps.interview.coding;

import com.lexisnexis.ps.interview.coding.model.Record;
import com.lexisnexis.ps.interview.coding.model.RecordRequest;

public interface IRecordService {
    Record request(RecordRequest request);
}
