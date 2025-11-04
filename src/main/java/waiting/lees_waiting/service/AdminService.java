package waiting.lees_waiting.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import waiting.lees_waiting.entity.Admin;
import waiting.lees_waiting.entity.RestaurantTable;
import waiting.lees_waiting.entity.WaitingInfo;
import waiting.lees_waiting.entity.WaitingStatus;
import waiting.lees_waiting.repository.AdminRepository;
import waiting.lees_waiting.repository.RestaurantTableRepository;
import waiting.lees_waiting.repository.WaitingInfoRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final WaitingInfoRepository waitingInfoRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    @PostConstruct
    public void initAdmin() {
        if (adminRepository.findByUsername("admin").isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("1234");
            adminRepository.save(admin);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Admin> login(String username, String password) {
        return adminRepository.findByUsername(username)
                .filter(admin -> admin.getPassword().equals(password));
    }

    public void createTables(int twoPersonTables, int fourPersonTables) {
        restaurantTableRepository.deleteAllInBatch();
        for (int i = 0; i < twoPersonTables; i++) {
            RestaurantTable table = new RestaurantTable();
            table.setCapacity(2);
            restaurantTableRepository.save(table);
        }
        for (int i = 0; i < fourPersonTables; i++) {
            RestaurantTable table = new RestaurantTable();
            table.setCapacity(4);
            restaurantTableRepository.save(table);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getTableCounts() {
        long twoPersonTables = restaurantTableRepository.countByCapacity(2);
        long fourPersonTables = restaurantTableRepository.countByCapacity(4);
        return Map.of("twoPersonTables", twoPersonTables, "fourPersonTables", fourPersonTables);
    }

    @Transactional(readOnly = true)
    public List<WaitingInfo> getWaitingList() {
        return waitingInfoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    public void updateTableEntryTime(Long tableId, LocalTime inputEntryTime) {
        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table Id:" + tableId));

        // 사용자가 입력한 시간을 서버의 현재 날짜와 결합하여 실제 입장 시각으로 설정
        // (과거 시간 입력 시 현재 시간으로 조정하는 로직 제거)
        LocalDateTime actualEntryDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), inputEntryTime);

        // estimatedEndTime은 실제 입장 시각으로부터 80분 후
        LocalDateTime estimatedEndTime = actualEntryDateTime.plusMinutes(80);
        
        // RestaurantTable에 저장 (entryTime은 실제 입장 시각의 시간 부분)
        table.setEntryTime(actualEntryDateTime.toLocalTime()); 
        table.setEstimatedEndTime(estimatedEndTime);
        
        restaurantTableRepository.save(table);
    }

    public void updateWaitingStatus(Long waitingId, WaitingStatus status) {
        WaitingInfo waitingInfo = waitingInfoRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid waiting Id:" + waitingId));
        waitingInfo.setStatus(status);
        waitingInfoRepository.save(waitingInfo);
    }

    public void clearAllData() {
        waitingInfoRepository.deleteAllInBatch();
        restaurantTableRepository.deleteAllInBatch();
    }

    public void clearTable(Long tableId) {
        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table Id:" + tableId));
        table.setEstimatedEndTime(null);
        table.setEntryTime(null); // Also clear the entry time when clearing the table
        restaurantTableRepository.save(table);
    }
}
