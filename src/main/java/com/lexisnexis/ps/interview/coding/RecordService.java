package com.lexisnexis.ps.interview.coding;

import com.lexisnexis.ps.interview.coding.model.*;
import com.lexisnexis.ps.interview.coding.repo.IArrestRecordRepository;
import com.lexisnexis.ps.interview.coding.repo.ICreditReportRepository;
import com.lexisnexis.ps.interview.coding.repo.IDriverReportRepository;
import com.lexisnexis.ps.interview.coding.repo.ILeinReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RecordService implements IRecordService {

  private ICreditReportRepository crr;
  private IRecordAccessRepository rar;
  private ILeinReportRepository lrr;
  private IDriverReportRepository drr;
  private RestTemplate restTemplate;
  private IArrestRecordRepository arr;

  @Autowired
  public RecordService(
      ICreditReportRepository crr,
      IRecordAccessRepository rar,
      ILeinReportRepository lrr,
      IDriverReportRepository drr,
      RestTemplate restTemplate,
      IArrestRecordRepository arr) {
    this.crr = crr;
    this.rar = rar;
    this.lrr = lrr;
    this.drr = drr;
    this.restTemplate = restTemplate;
    this.arr = arr;
  }

  @Override
  public Record request(RecordRequest request) {
    var type = request.getType();
    Record record = null;
    switch (type) {
      case Credit:
        record = readCreditReportFromDatabase(request);
        if (!check(request)) {
          throw new RecordAccessException();
        }
        log(request, record);
        return record;
      case Liens:
        record = readLienReportFromDatabase(request);
        if (!check(request)) {
          throw new RecordAccessException();
        }
        log(request, record);
        return record;
      case Driver:
        record = readDriverReport(request);
        if (!check(request)) {
          throw new RecordAccessException();
        }
        log(request, record);
        return record;
      case Bankruptcy:
        record = readBankruptcy(request);
        if (!check(request)) {
          throw new RecordAccessException();
        }
        log(request, record);
        return record;
      case JailBooking:
        record = readJailReport(request);
        if (!check(request)) {
          throw new RecordAccessException();
        }
        log(request, record);
        return record;
      case Arrest:
        record = new Record();
        ArrestRecord arrest = arr.findByArrestId(request.getRecordId());
        record.setRecordId(record.getRecordId());
        record.setOrgId(request.getOrgId());
        record.getSomeData().put("lname", arrest.getFname());
        record.getSomeData().put("fname", arrest.getLname());
        return record;
    }
    return record;
  }

  private Record readJailReport(RecordRequest request) {
    JailRequest jailRequest = new JailRequest();
    var jailResponse =
        restTemplate
            .postForEntity("https://10.0.0.6:8080/api/bancruptcy", jailRequest, JailRecord.class)
            .getBody();
    var record = new Record();
    record.setRecordId(request.getRecordId());
    record.setOrgId(request.getOrgId());
    record.getSomeData().put("duration", jailResponse.getDuration());
    return record;
  }

  private Record readBankruptcy(RecordRequest request) {
    BankruptcyRequest bankRequest = new BankruptcyRequest();
    bankRequest.setRecordId(request.getRecordId());
    var bankResponse =
        restTemplate
            .postForEntity(
                "https://10.1.0.6:8080/api/bancruptcy", bankRequest, BankruptcyRecord.class)
            .getBody();
    var record = new Record();
    record.setRecordId(request.getRecordId());
    record.setOrgId(request.getOrgId());
    record.getSomeData().put("date", bankResponse.getDate());
    return record;
  }

  private Record readDriverReport(RecordRequest request) {
    var state = request.getState();
    if (state != null
        && (state.toLowerCase().equals("california") || state.toUpperCase().equals("CA"))) {
      CADriverRequest caDriverRequest = new CADriverRequest();
      caDriverRequest.setRecordId(request.getRecordId());
      var driverRecord =
          restTemplate
              .postForEntity(
                  "https://10.1.0.6:8080/api/driver", caDriverRequest, DriverRecord.class)
              .getBody();
      var record = new Record();
      record.setRecordId(request.getRecordId());
      record.setOrgId(request.getOrgId());
      record.getSomeData().put("dlNumber", driverRecord.getDlNumber());
      return record;
    } else {
      DriverRecord driverRecord = drr.findByRecordId(request.getRecordId());
      var record = new Record();
      record.setRecordId(request.getRecordId());
      record.setOrgId(request.getOrgId());
      record.getSomeData().put("dlNumber", driverRecord.getDlNumber());
      return record;
    }
  }

  private Record readLienReportFromDatabase(RecordRequest request) {
    var lien = lrr.findByLeinId(request.getOrgId(), request.getRecordId());
    Record record = new Record();
    record.setRecordId(record.getRecordId());
    record.setOrgId(request.getOrgId());
    record.getSomeData().put("lienHolder", lien.getLienHolder());
    return record;
  }

  private void log(RecordRequest request, Record record) {
    var a = new RecordAccess();
    a.setRecordId(record.getRecordId());
    a.setOrgId(record.getOrgId());
    a.setGranted(check(request));
    boolean saved = rar.save(a);
  }

  private boolean check(RecordRequest request) {
    // check if this user is allowed to access this type of data
    User user = request.getUser();
    List<RecordType> u = user.getRecordTypeAccess();
    boolean allowed = false;
    for (int i = 0; i < u.size(); i++) if (u.get(i).equals(request.getType())) allowed = true;
    return allowed;
  }

  private Record readCreditReportFromDatabase(RecordRequest request) {
    var credit = crr.findByRecordId(request.getOrgId(), request.getRecordId());
    Record record = new Record();
    record.setRecordId(record.getRecordId());
    record.setOrgId(request.getOrgId());
    record.getSomeData().put("score", credit.getScore());
    record.getSomeData().put("openAccounts", credit.getOpenAccounts());
    return record;
  }
}
