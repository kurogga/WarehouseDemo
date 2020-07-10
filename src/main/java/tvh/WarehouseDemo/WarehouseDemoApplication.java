package tvh.WarehouseDemo;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;
import tvh.WarehouseDemo.application_logic.WarehouseService;
import tvh.WarehouseDemo.domain.Warehouse;
import tvh.WarehouseDemo.persistence.WarehouseRepository;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class WarehouseDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(WarehouseRepository repository, WarehouseService service) {
		return (args) -> {
			// create some warehouses with 100 empty spaces
			int amountOfWarehouses = 10;
			for (int i = 0; i < amountOfWarehouses; i++) {
				repository.save(new Warehouse(String.valueOf((char) (i + 65)), 100));
			}

			log.info("-------------------------------");
			// fetch all warehouses
			log.info("Warehouses found:");
			for (Warehouse result : repository.findAll())
				log.info(result.toString());

			log.info("-------------------------------");
			// add stock to a two warehouses
			log.info("Add stock to warehouses:");
			Random rand = new Random();
			int stocks = rand.nextInt(10) + 1;
			for (int i = 1; i < amountOfWarehouses + 1; i++) {
				Warehouse warehouse = repository.findByWarehouseId((long) i);
				warehouse.addStock("A", stocks);
				stocks = rand.nextInt(10) + 1;
				warehouse.addStock("B", stocks);
				stocks = rand.nextInt(10) + 1;
				warehouse.addStock("C", stocks);
				stocks = rand.nextInt(10) + 1;
				warehouse.addStock("D", stocks);
				stocks = rand.nextInt(10) + 1;
				repository.save(warehouse);
			}
			for (Warehouse result : repository.findAll())
				log.info(result.toString());

			log.info("-------------------------------");
			// move stocks between both warehouses
			Warehouse w1 = repository.findByWarehouseId((long) 1);
			Warehouse w2 = repository.findByWarehouseId((long) 2);
			boolean r1 = w1.moveStockTo(w2, "A", 5);
			boolean r2 = w2.moveStockTo(w1, "D", 1);
			log.info("Moved stock: " + r1 + r2);
			log.info(w1.toString());
			log.info(w2.toString());

			log.info("-------------------------------");
			// add email addresses to two warehouses
			w1.addEmailAddress("harry_tong_1994@hotmail.com");
			repository.save(w1);
			w2.addEmailAddress("harry_tong_1994@hotmail.com");
			w2.addEmailAddress("warehousemanagementdemo@gmail.com");
			w2.addEmailAddress("kuroggatong@gmail.com");
			repository.save(w2);
			log.info("Added email addresses to warehouse 1 and 2");
		};
	}
}
