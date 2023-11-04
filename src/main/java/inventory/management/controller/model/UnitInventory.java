package inventory.management.controller.model;

import java.util.Date;

import inventory.management.entity.Inventory;
import lombok.Data;
import lombok.NoArgsConstructor;

//inventory DTO or data transfer object
@Data
@NoArgsConstructor
public class UnitInventory {
	private Long inventoryId;
	private int inventoryQuantity;
	private Date inventoryLastUpdateTimestamp;
	
	public UnitInventory (Inventory inventory) {
		inventoryId = inventory.getInventoryId();
		inventoryQuantity = inventory.getInventoryQuantity();
		inventoryLastUpdateTimestamp = inventory.getInvetoryLastUpdateTimestamp();
	}
}
