package waiting.lees_waiting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import waiting.lees_waiting.entity.RestaurantTable;
import waiting.lees_waiting.entity.WaitingInfo;
import waiting.lees_waiting.entity.WaitingStatus;
import waiting.lees_waiting.repository.RestaurantTableRepository;
import waiting.lees_waiting.repository.WaitingInfoRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final WaitingInfoRepository waitingInfoRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public WaitingInfo registerWaiting(int people, String phone) {
        WaitingInfo waitingInfo = new WaitingInfo();
        waitingInfo.setPeople(people);
        waitingInfo.setPhone(phone);
        waitingInfo.setRegisteredAt(LocalDateTime.now());
        waitingInfo.setStatus(WaitingStatus.WAITING);
        return waitingInfoRepository.save(waitingInfo);
    }

    @Transactional(readOnly = true)
    public String getEstimatedWaitingTime(int people) {
        // 1. 그룹 정의
        int tableCapacity, minPeople, maxPeople;
        if (people <= 2) {
            tableCapacity = 2; minPeople = 1; maxPeople = 2;
        } else {
            tableCapacity = 4; minPeople = 3; maxPeople = 4;
        }

        // 2. 테이블 및 대기 순번 확인
        List<RestaurantTable> tables = restaurantTableRepository.findByCapacityOrderByEstimatedEndTimeAsc(tableCapacity);
        if (tables.isEmpty()) {
            return "이용 가능한 테이블이 없습니다.";
        }
        long myWaitingOrder = waitingInfoRepository.findAllByStatusAndPeopleBetween(WaitingStatus.WAITING, minPeople, maxPeople).size();

        // 3. 계산에 필요한 변수 설정
        int tableCount = tables.size();
        long rotationCount = (myWaitingOrder - 1) / tableCount;
        int tableIndex = (int) ((myWaitingOrder - 1) % tableCount);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime openingTime = now.toLocalDate().atTime(10, 0);

        // 4. 내가 이용할 테이블의 기본 입장 가능 시간 계산
        RestaurantTable myTable = tables.get(tableIndex);
        LocalDateTime baseAvailableTime = myTable.getEstimatedEndTime();

        // 4-1. 테이블이 현재 비어있다면, 현재 시간을 기준으로 함
        if (baseAvailableTime == null || baseAvailableTime.isBefore(now)) {
            baseAvailableTime = now;
        }

        // 4-2. 영업 시작 시간(10시) 제약 조건 적용
        boolean isWaitingForOpening = baseAvailableTime.isBefore(openingTime);
        if (isWaitingForOpening) {
            baseAvailableTime = openingTime;
        }

        // 5. 회전율을 고려하여 최종 예상 시간 계산
        LocalDateTime finalEstimatedTime = baseAvailableTime.plusMinutes(80 * rotationCount);

        // 6. 결과 메시지 포맷팅
        // 10시 이전에 대기 등록했고, 첫 회전 그룹에 속한다면 특별 메시지 출력
        if (isWaitingForOpening && rotationCount == 0) {
            return "10시 이후 입장 가능합니다";
        }

        // 계산된 시간이 현재와 거의 같다면 "바로 입장 가능"으로 안내
        if (finalEstimatedTime.isBefore(now.plusMinutes(1))) {
            return "바로 입장 가능합니다";
        }

        return finalEstimatedTime.format(DateTimeFormatter.ofPattern("HH시 mm분"));
    }
}
