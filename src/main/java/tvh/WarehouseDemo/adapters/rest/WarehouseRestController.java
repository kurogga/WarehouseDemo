package tvh.WarehouseDemo.adapters.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import tvh.WarehouseDemo.application_logic.WarehouseService;
import tvh.WarehouseDemo.domain.MoveStockRequest;
import tvh.WarehouseDemo.domain.Warehouse;

// Swagger UI for REST API 
// localhost:8081/swagger-ui/index.html
@RestController
@RequestMapping("/warehouses")
@Slf4j
public class WarehouseRestController {

	@Autowired
	WarehouseService warehouseService;

	@Operation(summary = "Get a list of all warehouses")
	@GetMapping
	public Iterable<Warehouse> getWarehouses() {
		return warehouseService.getWarehouses();
	}

	@Operation(summary = "Post a request to move stock between two warehouses")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Stock moved succesfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Iterable.class)) }),
			@ApiResponse(responseCode = "404", description = "Failed moving stock", content = @Content) })
	@PostMapping(value = "/movestock")
	public ResponseEntity<Iterable<Warehouse>> moveStock(@RequestBody MoveStockRequest request) {
		log.info("POST /warehouses/movestock: {}", request.toString());
		Iterable<Warehouse> results = warehouseService.moveStock(request);
		if (results == null) {
			log.error("Failed moving stock");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("reason", "Failed moving stock").body(null);
		}
		return ResponseEntity.ok(results);
	}

	@Operation(summary = "Get the remaining space in the warehouse")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the warehouse's space", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)) }),
			@ApiResponse(responseCode = "404", description = "Warehouse's id doesn't exist", content = @Content) })
	@GetMapping("/{id}/stocks")
	public ResponseEntity<Map<String, Integer>> getStocks(@PathVariable("id") Long id) {
		log.info("GET /warehouses/{}/stocks", id);
		Warehouse w = warehouseService.getWarehouse(id);
		if (w == null) {
			log.error("Warehouse's id doesn't exist");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("reason", "Warehouse's id doesn't exist")
					.body(null);
		}
		return ResponseEntity.ok(w.getQuantityByItemID());
	}

	@Operation(summary = "Post request to add an email address to subscribe to the warehouse's daily")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Email address succesfully added", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Warehouse.class)) }),
			@ApiResponse(responseCode = "400", description = "Given email address already added", content = @Content),
			@ApiResponse(responseCode = "404", description = "Warehouse's id doesn't exist", content = @Content) })
	@PostMapping(value = "/{id}")
	public ResponseEntity<Warehouse> addEmailAddress(@PathVariable("id") Long id, @RequestBody String email) {
		log.info("POST /warehouses/{}: {}", id, email);
		if (warehouseService.getWarehouse(id) == null) {
			log.error("Warehouse's id doesn't exist");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("reason", "Warehouse's id doesn't exist")
					.body(null);
		}
		Warehouse w = warehouseService.addEmailAddress(id, email);
		if (w == null) {
			log.error("Given email address already added");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("reason", "Given email address already added")
					.body(null);
		}
		return ResponseEntity.ok(w);
	}
}
