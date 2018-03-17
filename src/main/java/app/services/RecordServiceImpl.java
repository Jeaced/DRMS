package app.services;

import app.models.Record;
import app.models.User;
import app.repositories.RecordRepository;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Record> getRecordList(String username) {
        return recordRepository.findAllByOwner_username(username);
    }

    @Override
    public void saveNewRecord(String username, String text) {
        Record record = new Record();
        User user = userRepository.findByUsername(username);
        record.setOwner(user);
        record.setText(text);
        recordRepository.saveAndFlush(record);

    }
}
