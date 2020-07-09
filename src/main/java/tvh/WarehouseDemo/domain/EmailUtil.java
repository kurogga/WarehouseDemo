package tvh.WarehouseDemo.domain;

import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailUtil {
	public static void sendEmail(Session session, String toEmail, List<String> bCCEmails, String subject, String body) {
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress("warehousemanagementdemo@gmail.com", "Warehouse Management Demo"));
			msg.setReplyTo(InternetAddress.parse("warehousemanagementdemo@gmail.com", false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			if (!bCCEmails.isEmpty()) {
				for (String bcc : bCCEmails) {
					msg.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
				}
			}
			log.info("Message is ready");
			Transport.send(msg);
			log.info("Email Sent Successfully!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
