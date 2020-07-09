package tvh.WarehouseDemo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tvh.WarehouseDemo.domain.Warehouse;

public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {

	Warehouse findByWarehouseId(Long warehouseId);

	@Query("SELECT w FROM Warehouse w WHERE w.emailAddresses IS NOT EMPTY")
	List<Warehouse> findByEmailAddressesNotEmpty();
}
