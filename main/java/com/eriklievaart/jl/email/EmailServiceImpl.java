package com.eriklievaart.jl.email;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.eriklievaart.jl.bundle.api.MultiPartParameter;
import com.eriklievaart.jl.email.api.Email;
import com.eriklievaart.jl.email.api.EmailService;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class EmailServiceImpl implements EmailService {
	private LogTemplate log = new LogTemplate(getClass());

	private AtomicReference<String> fallbackReplyAddress = new AtomicReference<>("noreply@example.com");
	private AtomicReference<String> host = new AtomicReference<>("localhost");

	@Override
	public void setFallbackReplyToAddress(String email) {
		log.info("default reply addres: $", email);
		fallbackReplyAddress.set(email);
	}

	@Override
	public void send(Email email) {
		try {
			if (Str.isBlank(email.getFrom())) {
				email.setFrom(fallbackReplyAddress.get());
			}
			Transport.send(createMessage(email));

		} catch (Exception e) {
			log.warn("unable to send email: $", e, e.getMessage());
			log.debug(email);
		}
	}

	private Message createMessage(Email email) throws Exception {
		Check.notNull(email, email.getSubject(), email.getBody());

		Message msg = createSingleOrMultiPartMessage(email.getAttachment(), email.getBody());

		msg.setFrom(new InternetAddress(email.getFrom()));
		msg.setRecipients(Message.RecipientType.TO, parseEmails(email.getTo()));
		msg.setRecipients(Message.RecipientType.CC, parseEmails(email.getCc()));
		msg.setRecipients(Message.RecipientType.BCC, parseEmails(email.getBcc()));
		msg.setSubject(email.getSubject());
		msg.setSentDate(new Date());

		return msg;
	}

	private Message createSingleOrMultiPartMessage(MultiPartParameter mpp, String body) throws Exception {
		if (mpp == null || mpp.getSize() == 0) {
			return createSinglePartMessage(body);
		}
		return createMultiPartMessage(mpp, body);
	}

	private Message createSinglePartMessage(String body) throws MessagingException {
		log.trace("plain text body => single part message");
		Message msg = new MimeMessage(createMailSession());
		msg.setText(body);
		return msg;
	}

	private Message createMultiPartMessage(MultiPartParameter mpp, String body) throws Exception {
		Message msg = new MimeMessage(createMailSession());

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(createTextPart(body));
		multipart.addBodyPart(createAttachmentPart(mpp));
		msg.setContent(multipart);

		return msg;
	}

	private MimeBodyPart createTextPart(String body) throws MessagingException {
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setText(body);
		return bodyPart;
	}

	private MimeBodyPart createAttachmentPart(MultiPartParameter attach) throws MessagingException, IOException {
		MimeBodyPart bodyPart = new MimeBodyPart();
		String mime = new MimetypesFileTypeMap().getContentType(attach.getName());
		bodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attach.getInputStream(), mime)));
		bodyPart.setFileName(attach.getName());

		log.debug("multipart body for attachment % of mime type %", attach.getName(), mime);
		return bodyPart;
	}

	private InternetAddress[] parseEmails(String addresses) throws AddressException {
		return addresses == null ? new InternetAddress[0] : InternetAddress.parse(addresses);
	}

	private Session createMailSession() {
		Properties props = new Properties();

		props.put("mail.smtp.host", host.get());
		props.put("mail.debug", "false");

		return Session.getInstance(props);
	}

	public void setHost(String value) {
		host.set(value);
	}
}