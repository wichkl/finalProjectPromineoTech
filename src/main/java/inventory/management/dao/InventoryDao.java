package inventory.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import inventory.management.entity.Inventory;

public interface InventoryDao extends JpaRepository<Inventory, Long> {

}
