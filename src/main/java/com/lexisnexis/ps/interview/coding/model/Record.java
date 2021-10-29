package com.lexisnexis.ps.interview.coding.model;

import java.util.HashMap;
import java.util.Map;

public class Record {
    private long orgId;
    private long recordId;
    private Map<String, Object> someData = new HashMap<>();


    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public Map<String, Object> getSomeData() {
        return someData;
    }

    public void setSomeData(Map<String, Object> someData) {
        this.someData = someData;
    }
}
