package com.lexisnexis.ps.interview.coding.model;

import java.util.*;

public class User {
    private List<RecordType> recordTypeAccess = new ArrayList<>();

    public List<RecordType> getRecordTypeAccess() {
        return recordTypeAccess;
    }

    public void setRecordTypeAccess(List<RecordType> recordTypeAccess) {
        this.recordTypeAccess = recordTypeAccess;
    }
}
