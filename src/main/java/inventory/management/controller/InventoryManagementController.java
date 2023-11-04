package inventory.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import inventory.management.controller.model.UnitData;
import inventory.management.controller.model.UnitInventory;
import inventory.management.controller.model.UnitManufacturer;
import inventory.management.service.InventoryManagementService;
import lombok.extern.slf4j.Slf4j;


//the HTTP endpoints and the interface to interact with the program
@RestController
@RequestMapping("/inventory_management")
@Slf4j
public class InventoryManagementController {
	@Autowired
	private InventoryManagementService inventoryManagementService;
	
	@PostMapping("/units")
	@ResponseStatus(code = HttpStatus.CREATED)
	public UnitData createInventoryManager(@RequestBody UnitData unitData) {
		log.info("Create a new unit: {}", unitData);
		return inventoryManagementService.createUnit(unitData);
	}
	@PutMapping("/units/{unitId}")
	public UnitData updateUnit (@PathVariable Long unitId, @RequestBody UnitData unitData) {
		unitData.setUnitId(unitId);
		log.info("Updating Unit {}", unitData);
		return inventoryManagementService.createUnit(unitData);
	}
	@PostMapping("/units/{unitId}/inventory")
	@ResponseStatus(code = HttpStatus.CREATED)
	public UnitInventory addInventory (@PathVariable Long unitId, 
			@RequestBody UnitInventory unitInventory) {
		log.info("Adding inventory {}", unitInventory);
		return inventoryManagementService.saveInventory(unitId, unitInventory);
	}
	@PostMapping("/units/{unitId}/manufacturer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public UnitManufacturer addManufacturer (@PathVariable Long unitId, 
			@RequestBody UnitManufacturer unitManufacturer) {
		log.info("Adding manufacturer {}", unitManufacturer);
		return inventoryManagementService.saveManufacturer(unitId, unitManufacturer);
	}
	@GetMapping("/units")
	public List<UnitData> retrieveAllUnits() {
		return inventoryManagementService.retrieveAllUnits();
	}
	@GetMapping("/units/{unitId}")
	public UnitData getUnitById(@PathVariable Long unitId) {
		log.info("Retrieving Unit with ID={}", unitId);
		return inventoryManagementService.retrieveUnitById(unitId);
	}
	@DeleteMapping("/units/{unitId}")
	public Map<String, String> deleteUnitById(@PathVariable Long unitId) {
		log.info("Deleting Unit with ID={}", unitId);
		inventoryManagementService.deleteUnitById(unitId);
		return Map.of("message", "Deletion of Unit with ID=" + unitId + " was successful");
	}
	@DeleteMapping("/units/{unitId}/inventory/{inventoryId}")
	public Map<String, String> deleteInventoryById(@PathVariable Long unitId, 
			@PathVariable Long inventoryId) {
		log.info("Deleting Inventory with ID={} from Unit ID={}", inventoryId, unitId);
		inventoryManagementService.deleteInventoryById(unitId, inventoryId);
		return Map.of("message", "Deletion of Inventory with ID=" + inventoryId + 
				" from Unit with ID=" + unitId + " was successful");
	}
}
