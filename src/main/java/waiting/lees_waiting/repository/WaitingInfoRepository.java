package waiting.lees_waiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import waiting.lees_waiting.entity.WaitingInfo;
import waiting.lees_waiting.entity.WaitingStatus;

import java.util.List;

public interface WaitingInfoRepository extends JpaRepository<WaitingInfo, Long> {
    List<WaitingInfo> findAllByStatusAndPeopleBetween(WaitingStatus status, int minPeople, int maxPeople);
}
