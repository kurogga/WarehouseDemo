package tvh.WarehouseDemo.application_logic;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	WarehouseService service;

	@Scheduled(cron = "0 0 8 * * ?")
	public void reportWarehouseVolume() {
		log.info("Sending daily warehouse volume report {}", dateFormat.format(new Date()));
		service.sendMail();
	}
}
