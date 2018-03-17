package app.services;

import app.models.Record;

import java.util.List;

public interface RecordService {

    List<Record> getRecordList(String username);

    void saveNewRecord(String username, String text);
}
