package inventory.management.controller.model;

import inventory.management.entity.Manufacturer;
import lombok.Data;
import lombok.NoArgsConstructor;

//manufacturer DTO or data transfer object
@Data
@NoArgsConstructor
public class UnitManufacturer {
	private Long manufacturerId;
	private String manufacturerName;
	private String manufacturerContact;
	
	public UnitManufacturer(Manufacturer manufacturer) {
		manufacturerId = manufacturer.getManufacturerId();
		manufacturerName = manufacturer.getManufacturerName();
		manufacturerContact = manufacturer.getManufacturerContact();
	}
}
