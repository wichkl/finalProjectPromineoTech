package inventory.management.controller.model;

import java.util.HashSet;
import java.util.Set;

import inventory.management.entity.Inventory;
import inventory.management.entity.Manufacturer;
import inventory.management.entity.Units;
import lombok.Data;
import lombok.NoArgsConstructor;

//unit DTO or data transfer object
@Data
@NoArgsConstructor
public class UnitData {
	private Long unitId;
	private String unitName;
	private String unitDescription;
	private double unitPrice;
	private int unitQuantity;
	private String unitCategory;
	
	private Set<UnitManufacturer> manufacturers = new HashSet<>();
	
	private Set<UnitInventory> inventorys = new HashSet<>();
	
	public UnitData(Units units) {
		for(Manufacturer manufacturer : units.getManufacturer()) {
			manufacturers.add(new UnitManufacturer(manufacturer));
		}
		for(Inventory inventory : units.getInventory()) {
			inventorys.add(new UnitInventory(inventory));
		}
	}
}
