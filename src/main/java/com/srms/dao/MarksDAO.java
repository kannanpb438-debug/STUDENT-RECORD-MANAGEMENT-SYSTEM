package com.srms.dao;

import com.srms.model.Mark;
import java.util.List;

public interface MarksDAO {
    boolean saveOrUpdateMarksBatch(List<Mark> marksList);
    List<Mark> getMarksByStudent(int studentId);
    List<Mark> getMarksByStudentAndSubject(int studentId, int subjectId);
    List<Mark> getMarksBySubjectAndExam(int subjectId, String examType);
    double getAverageMarksBySubject(int subjectId, String examType);
    double getStudentSessionAverage(int studentId);
    double getStudentEndExamAverage(int studentId);
}

