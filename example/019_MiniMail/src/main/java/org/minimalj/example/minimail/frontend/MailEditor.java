package org.minimalj.example.minimail.frontend;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import org.minimalj.backend.Backend;
import org.minimalj.example.minimail.backend.MailService;
import org.minimalj.example.minimail.model.Mail;
import org.minimalj.frontend.editor.Editor.SimpleEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.StringFormElement;
import org.minimalj.model.properties.Properties;

public class MailEditor extends SimpleEditor<Mail> {
	
	private static final Logger LOGGER = Logger.getLogger(Properties.class.getName());

	private final class MailForm extends Form<Mail> {
		public MailForm() {
			line(new StringFormElement(Mail.$.from.address, StringFormElement.SINGLE_LINE));
			line(new StringFormElement(Mail.$.to.address, StringFormElement.SINGLE_LINE));
			line(Mail.$.date);
			line(new StringFormElement(Mail.$.subject, StringFormElement.SINGLE_LINE));
			line(Mail.$.text);
		}
		
		@Override
		protected int getColumnWidthPercentage() {
			return 300;
		}
	}

	@Override
	protected Mail createObject() {
		return new Mail();
	}

	@Override
	protected Form<Mail> createForm() {
		return new MailForm();
	}

	@Override
	protected Mail save(Mail object) {
		try {
			MailService.sendMail(object);
		} catch (MessagingException e) {
			LOGGER.log(Level.SEVERE, String.format("unable to send mail to recipient %s", object.from.address), e);
		}
		return Backend.save(object);
	}

}
