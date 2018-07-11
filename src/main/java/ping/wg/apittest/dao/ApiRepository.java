package ping.wg.apittest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ping.wg.apittest.entity.ApiEntity;

public interface ApiRepository extends JpaRepository<ApiEntity, Long> {
}
