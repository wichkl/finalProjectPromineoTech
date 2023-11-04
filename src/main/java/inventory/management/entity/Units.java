package inventory.management.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//setup of the unit entity
@Entity
@Data
public class Units {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long unitId;
	private String unitName;
	private String unitDescription;
	private double unitPrice;
	private int unitQuantity;
	private String unitCategory;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "units_manufacturer", 
		joinColumns = @JoinColumn(name = "unit_id"), 
		inverseJoinColumns = @JoinColumn(name = "manufacturer_id"))
	private Set<Manufacturer> manufacturer = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "units", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Inventory> inventory = new HashSet<>();
}
