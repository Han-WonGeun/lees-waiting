package waiting.lees_waiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import waiting.lees_waiting.entity.RestaurantTable;

import java.util.List;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    // 특정 capacity를 가진 테이블들을 estimatedEndTime 오름차순으로 정렬하여 반환
    List<RestaurantTable> findByCapacityOrderByEstimatedEndTimeAsc(int capacity);

    // 여러 capacity를 가진 테이블들을 estimatedEndTime 오름차순으로 정렬하여 반환 (이전 로직용)
    List<RestaurantTable> findByCapacityInOrderByEstimatedEndTimeAsc(List<Integer> capacities);

    // 특정 capacity보다 크거나 같은 테이블 중 가장 빨리 비는 테이블 하나를 반환 (이전 로직용)
    Optional<RestaurantTable> findFirstByCapacityGreaterThanEqualOrderByEstimatedEndTimeAsc(int capacity);

    // 특정 capacity를 가진 테이블의 개수를 반환
    long countByCapacity(int capacity);
}
