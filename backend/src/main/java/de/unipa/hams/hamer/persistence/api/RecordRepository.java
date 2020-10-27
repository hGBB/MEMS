package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.Kpi;
import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

  Record getByTimestampAndType(Date date, Kpi type);

  List<Record> getAllByTimestampAfterAndSentBy(Date date, Node node);

  List<Record> getAllByTimestampBetweenAndSentBy(Date start, Date end, Node node);

  List<Record> getAllByTimestampAfterAndSentByAndType(Date date, Node node, Kpi type);

  void deleteAllBySentBy(Node sentBy);

}
