package tvh.WarehouseDemo.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
public class Warehouse {

	@Id
	@GeneratedValue
	private Long warehouseId;
	private String name;
	private int volume;
	private int remainingSpace;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_QUANTITY", joinColumns = @JoinColumn(name = "WAREHOUSE_ID"))
	@MapKeyJoinColumn(name = "ITEM")
	@JsonManagedReference
	private Map<String, Integer> quantityByItemID;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> emailAddresses;

	public Warehouse() {
		this.quantityByItemID = new HashMap<String, Integer>();
		this.emailAddresses = new ArrayList<>();
	}

	public Warehouse(String name, int volume) {
		this.name = name;
		this.volume = volume;
		this.remainingSpace = volume;
		this.quantityByItemID = new HashMap<String, Integer>();
		this.emailAddresses = new ArrayList<>();
	}

	// Add stock if there are space remaining
	public boolean addStock(String name, int quantity) {
		if (this.remainingSpace < quantity)
			return false;
		this.remainingSpace -= quantity;
		int toAddAmount = quantity;
		if (this.quantityByItemID.containsKey(name)) {
			toAddAmount += this.quantityByItemID.get(name);
		}
		this.quantityByItemID.put(name, toAddAmount);
		return true;
	}

	// Remove stock if it exists and has enough
	public boolean removeStock(String name, int quantity) {
		if (this.quantityByItemID.containsKey(name) && this.quantityByItemID.get(name) >= quantity) {
			this.remainingSpace += quantity;
			this.quantityByItemID.put(name, this.quantityByItemID.get(name) - quantity);
			return true;
		}
		return false;
	}

	// Move stock from this warehouse to another warehouse if both condition are
	// satisfied
	public boolean moveStockTo(Warehouse warehouse, String name, int quantity) {
		if (warehouse.addStock(name, quantity)) {
			if (this.removeStock(name, quantity)) {
				return true;
			}
			warehouse.removeStock(name, quantity);
		}
		return false;
	}

	// Add email address for subscription to daily warehouse remaining space report
	public boolean addEmailAddress(String email) {
		if (this.emailAddresses.contains(email))
			return false;
		this.emailAddresses.add(email);
		return true;
	}

}
