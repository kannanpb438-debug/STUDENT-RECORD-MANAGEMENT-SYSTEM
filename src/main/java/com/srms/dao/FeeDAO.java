package com.srms.dao;

import com.srms.model.FeeRecord;
import java.util.List;

public interface FeeDAO {
    List<FeeRecord> getFeeRecordsByStudent(int studentId);
    FeeRecord getFeeRecordById(int feeId);
    List<FeeRecord> getAllFeeRecords();
    boolean addFeeRecord(FeeRecord fee);
    boolean markAsPaid(int feeId, String receiptNo);
    int getPendingFeeCount();
    double getTotalPendingAmount();
}
