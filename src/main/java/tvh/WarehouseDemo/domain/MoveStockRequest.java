package tvh.WarehouseDemo.domain;

import lombok.Data;

@Data
public class MoveStockRequest {
	private Long warehouseFrom;
	private Long warehouseTo;
	private String item;
	private int quantity;

	public MoveStockRequest() {
	}

	public MoveStockRequest(Long warehouseFrom, Long warehouseTo, String item, int quantity) {
		this.warehouseFrom = warehouseFrom;
		this.warehouseTo = warehouseTo;
		this.item = item;
		this.quantity = quantity;
	}
}
