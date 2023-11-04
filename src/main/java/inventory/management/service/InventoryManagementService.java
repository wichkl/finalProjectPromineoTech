package inventory.management.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import inventory.management.controller.model.UnitData;
import inventory.management.controller.model.UnitInventory;
import inventory.management.controller.model.UnitManufacturer;
import inventory.management.dao.InventoryDao;
import inventory.management.dao.ManufacturerDao;
import inventory.management.dao.UnitsDao;
import inventory.management.entity.Inventory;
import inventory.management.entity.Manufacturer;
import inventory.management.entity.Units;

@Service
public class InventoryManagementService {
	//connections to the dao layers
	@Autowired
	private UnitsDao unitsDao;
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private ManufacturerDao manufacturerDao;
	
	//create a new unit
	public UnitData createUnit(UnitData unitData) {
		Long unitId = unitData.getUnitId();
		Units unit = findOrCreateUnit(unitId);
		
		copyUnitFields(unit, unitData);
		updateInventoryQuantity(unit);
		return new UnitData(unitsDao.save(unit));
	}
	//transfer information from the entity
	private void copyUnitFields(Units unit, UnitData unitData) {
		unit.setUnitId(unitData.getUnitId());
		unit.setUnitName(unitData.getUnitName());
		unit.setUnitDescription(unitData.getUnitDescription());
		unit.setUnitCategory(unitData.getUnitCategory());
		unit.setUnitPrice(unitData.getUnitPrice());
		unit.setUnitQuantity(unitData.getUnitQuantity());
		
	}
	//find the unit by id or create a new unit if the unit doesnt exist
	private Units findOrCreateUnit(Long unitId) {
		Units unit;
		if(Objects.isNull(unitId)) {
			unit = new Units();
		} else { 
			unit = findUnitById(unitId);
		}
		return unit;
	}
	//find the unit by the incremented id
	public Units findUnitById(Long unitId) {
		return unitsDao.findById(unitId)
				.orElseThrow(() -> new NoSuchElementException(
						"Unit with ID=" + unitId + "was not found"));
	}
	/*
	 * creation of a new inventory for the unit entity
	 * implementation of the auto updating the inventoryQuantity
	 * also sets the date automatically
	 * there is no remove x number from inventory outside of deleting an inventory id
	 */
	public UnitInventory saveInventory(Long unitId, UnitInventory unitInventory) {
		Units unit = findUnitById(unitId);
		Inventory inventory;
		
		if(unitInventory.getInventoryId() != null) {
			inventory = findInventoryById(unitId, unitInventory.getInventoryId());
		} else {
			inventory = new Inventory();
		}
		copyInventoryFields(inventory, unitInventory);
		inventory.setUnits(unit);
		
		inventory.setInvetoryLastUpdateTimestamp(new Date());
		
		unit.getInventory().add(inventory);
		inventoryDao.save(inventory);
		
		updateInventoryQuantity(unit);
		
		return new UnitInventory(inventory);
	}
	/*
	 * recalculate and updating inventoryQuantity
	 * there is no process of removing count as of yet
	 * the though process was inventory id was like a pallet id
	 */
	private void updateInventoryQuantity(Units unit) {
		int totalUnitQuantity = calculateTotalUnitQuantity(unit);
		for(Inventory inventory : unit.getInventory()) {
			inventory.setInventoryQuantity(totalUnitQuantity);
		}
		inventoryDao.saveAll(unit.getInventory());
	}
	//calculates the total unit quantities for a given unit entity
	private int calculateTotalUnitQuantity(Units unit) {
		int totalUnitQuantity = 0;
		for(Inventory inventory : unit.getInventory()) {
			totalUnitQuantity += inventory.getUnits().getUnitQuantity();
		}
		return totalUnitQuantity;
	}
	//transfer information from the entity
	private void copyInventoryFields(Inventory inventory, UnitInventory unitInventory) {
		inventory.setInventoryId(unitInventory.getInventoryId());
		inventory.setInventoryQuantity(unitInventory.getInventoryQuantity());
		inventory.setInvetoryLastUpdateTimestamp(unitInventory.getInventoryLastUpdateTimestamp());
	}
	//find the inventory by ID
	private Inventory findInventoryById(Long unitId, Long inventoryId) {
		Inventory inventory = inventoryDao.findById(inventoryId)
				.orElseThrow(() -> new NoSuchElementException(
						"Inventory with ID=" + inventoryId + " was not found"));
		if(inventory.getUnits().getUnitId().equals(unitId)) {
			return inventory;
		} else {
			throw new IllegalArgumentException("Inventory with ID=" 
					+ inventoryId + " does not belong to Unit ID=" + unitId);
		}
	}
	//creation of a new manufacturer for the unit entity
	public UnitManufacturer saveManufacturer(Long unitId, UnitManufacturer unitManufacturer) {
		Units unit = findUnitById(unitId);
		Manufacturer manufacturer;
		
		if(unitManufacturer.getManufacturerId() != null) {
			manufacturer = findManufacturerById(unitId, unitManufacturer.getManufacturerId());
		} else {
			manufacturer = new Manufacturer();
		}
		copyManufacturerFields(manufacturer, unitManufacturer);
		manufacturer.getUnits().add(unit);
		unit.getManufacturer().add(manufacturer);
		manufacturerDao.save(manufacturer);
		return new UnitManufacturer(manufacturer);
	}
	//transfer information from the entity
	private void copyManufacturerFields(Manufacturer manufacturer, UnitManufacturer unitManufacturer) {
		manufacturer.setManufacturerId(unitManufacturer.getManufacturerId());
		manufacturer.setManufacturerName(unitManufacturer.getManufacturerName());
		manufacturer.setManufacturerContact(unitManufacturer.getManufacturerContact());
	}
	/*
	 * find the manufacturer by ID
	 * contains a for loop to search through the unit as this is a many to many relationship
	 */
	private Manufacturer findManufacturerById (Long unitId, Long manufacturerId) {
		Manufacturer manufacturer = manufacturerDao.findById(manufacturerId)
				.orElseThrow(() -> new NoSuchElementException(
						"Manufacturer with ID=" + manufacturerId + " was not found"));
		boolean found = false;
		
		for(Units unit : manufacturer.getUnits()) {
			if(unit.getUnitId().equals(unitId)) {
				found = true;
				break;
			}
		}
		if(!found) {
			throw new IllegalArgumentException("Manufacturer with ID=" 
					+ manufacturerId + " does not belong to Unit ID=" + unitId);
		}
		return manufacturer;
	}
	//to list all units
	@Transactional(readOnly = false)
	public List<UnitData> retrieveAllUnits() {
		List<Units> units = unitsDao.findAll();
		List<UnitData> result = new LinkedList<>();
		
		for(Units unit : units) {
			UnitData ud = new UnitData(unit);
			ud.getInventorys().clear();
			ud.getManufacturers().clear();
			result.add(ud);
		}
		return result;
	}
	//to get a unit and information based on the ID
	@Transactional(readOnly = true)
	public UnitData retrieveUnitById(Long unitId) {
		Units unit = unitsDao.findById(unitId)
				.orElseThrow(() -> new NoSuchElementException(
						"Unit with ID=" + unitId + " was not found"));
		return new UnitData(unit);
	}
	//delete a single unit
	@Transactional(readOnly = false)
	public void deleteUnitById(Long unitId) {
		Units unit = findUnitById(unitId);
		unitsDao.delete(unit);
	}
	//delete an inventory ID connected to a unit
	@Transactional(readOnly = false)
	public void deleteInventoryById(Long unitId, Long inventoryId) {
		Units unit = findUnitById(unitId);
		Inventory inventory = findInventoryById(unitId, inventoryId);
		unit.getInventory().remove(inventory);
		inventoryDao.delete(inventory);
	}

}
