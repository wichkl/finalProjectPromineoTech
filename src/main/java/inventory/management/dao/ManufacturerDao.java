package inventory.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import inventory.management.entity.Manufacturer;

public interface ManufacturerDao extends JpaRepository<Manufacturer, Long> {

}
