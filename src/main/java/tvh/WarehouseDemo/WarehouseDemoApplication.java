package tvh.WarehouseDemo;

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
			Warehouse w1 = repository.findByWarehouseId((long) 1);
			Warehouse w2 = repository.findByWarehouseId((long) 2);
			w1.addStock("A", 5);
			w1.addStock("B", 50);
			w1.addStock("C", 2);
			w1.addStock("D", 9);
			w2.addStock("A", 4);
			w2.addStock("B", 30);
			w2.addStock("C", 20);
			w2.addStock("D", 11);
			repository.save(w1);
			repository.save(w2);
			log.info("Added stock to warehouses:");
			log.info(w1.toString());
			log.info(w2.toString());

			log.info("-------------------------------");
			// move stocks between both warehouses
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
