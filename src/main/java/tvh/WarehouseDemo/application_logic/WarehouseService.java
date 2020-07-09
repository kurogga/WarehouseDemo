package tvh.WarehouseDemo.application_logic;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tvh.WarehouseDemo.domain.EmailUtil;
import tvh.WarehouseDemo.domain.MoveStockRequest;
import tvh.WarehouseDemo.domain.Warehouse;
import tvh.WarehouseDemo.persistence.WarehouseRepository;

@Component
@Slf4j
public class WarehouseService {
	@Autowired
	WarehouseRepository repository;
	private static final String USERNAME = "warehousemanagementdemo@gmail.com";
	private static final String PASSWORD = "demotvh123";

	// Add stock to a warehouse
	public Warehouse addStock(Long warehouseId, String name, int quantity) {
		Warehouse w = repository.findByWarehouseId(warehouseId);
		if (!w.addStock(name, quantity)) {
			log.error("Warehouse {}: no space available!", warehouseId);
			return null;
		}
		repository.save(w);
		return w;
	}

	// Remove stock from a warehouse
	public Warehouse removeStock(Long warehouseId, String name, int quantity) {
		Warehouse w = repository.findByWarehouseId(warehouseId);
		if (!w.removeStock(name, quantity)) {
			log.error("Warehouse {}: cant remove stock!", warehouseId);
			return null;
		}
		repository.save(w);
		return w;
	}

	// Move stock between two warehouses
	public Iterable<Warehouse> moveStock(MoveStockRequest request) {
		log.info("Moving stock: {}", request.toString());
		if (request.getWarehouseFrom() == request.getWarehouseTo())
			return null;
		Warehouse from = repository.findByWarehouseId(request.getWarehouseFrom());
		Warehouse to = repository.findByWarehouseId(request.getWarehouseTo());
		if (!from.moveStockTo(to, request.getItem(), request.getQuantity()))
			return null;
		repository.save(from);
		repository.save(to);
		return Arrays.asList(from, to);
	}

	// Add email address to a warehouse's subscriber list
	public Warehouse addEmailAddress(Long warehouseId, String email) {
		Warehouse w = repository.findByWarehouseId(warehouseId);
		if (!w.addEmailAddress(email)) {
			log.error("Warehouse {}: email address is already added!", warehouseId);
			return null;
		}
		repository.save(w);
		return w;
	}

	// Send email to each subscribed email addresses
	public void sendMail() {
		String toEmail = USERNAME;
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});
		List<Warehouse> warehouses = repository.findByEmailAddressesNotEmpty();
		for (Warehouse w : warehouses) {
			String msgSubject = "Warehouse " + w.getName() + ": Daily volume report";
			String msgBody = "WAREHOUSE " + w.getName() + " REMAINING SPACE: " + w.getRemainingSpace() + " / "
					+ w.getVolume();
			EmailUtil.sendEmail(session, toEmail, w.getEmailAddresses(), msgSubject, msgBody);
		}
	}

	public Iterable<Warehouse> getWarehouses() {
		return repository.findAll();
	}

	public Warehouse getWarehouse(Long id) {
		return repository.findByWarehouseId(id);
	}

}
