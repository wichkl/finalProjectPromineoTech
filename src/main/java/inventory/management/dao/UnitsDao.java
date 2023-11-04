package inventory.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import inventory.management.entity.Units;

public interface UnitsDao extends JpaRepository<Units, Long> {
	
}
