package com.yidatec.weixin.mail;

import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * Class to send email
 */
public class SimpleMessageSender extends AbstractMessageSender {
	
	private static final Logger log = LogManager.getLogger(SimpleMessageSender.class);
	
	/**
	 * 发邮件
	 * @param mail
	 */
	public void sendMail(MailEntity mail) {
		try {
			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
			//1.检查mail实体对象是否为空,为空跳过.
			if (mail == null) {
				log.error("mail实体对象为空");
				return;
			}
			//2.检查接收者邮箱是否有效,无效跳过.
			if (!mail.isValidTo()) {
				for (int i=0; i<mail.getTo().length; i++) {
					log.error("接收者邮箱无效:"+mail.getTo()[i]);
				}				
				return;
			} else {
				helper.setTo(mail.getTo());
			}
			//3.检查发送者邮箱是否有效,无效的话,使用默认的邮箱.
			if (mail.isValidFrom()) {
				helper.setFrom(mail.getFrom());
			} else {
				helper.setFrom(from);
			}
			//4.检查是否含有非法的抄送者, 如果全部合法设置CC
			if (mail.isValidCc()) {
				helper.setCc(mail.getCc());
			}
			//5.检查是否含有非法的密抄送者, 如果全部合法设置BCC
			if (mail.isValidBcc()) {
				helper.setBcc(mail.getBcc());
			}
			//6.设置邮件的主题
			helper.setSubject(mail.getSubject());
			//7.设置邮件的内容
			helper.setText(mail.getContent(), mail.isHtml());
			//8.发送邮件
			sender.send(msg);
			for (int i=0; i<mail.getTo().length; i++) {
				log.info("Send mail to : " + mail.getTo()[i]);
			}
		}
		catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}

    /**
     * Method to send email
     * @param to			The to address
     * @param subject		Subject of the mail
     * @param text			Body of the mail
     * @throws Exception
     */
	public void sendHtmlMessage(String to, String subject, String text) throws Exception {
		try {
			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);		
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setText(text, true);
			sender.send(msg);
		}
		catch (Exception e){
			log.error(e.getMessage());
		}
	}
   
    /**
     * Method to send email
     * @param from			The sender email address
     * @param to			The receiver address
     * @param subject		Subject of the mail
     * @param text			Body of the mail
     * @throws Exception
     */
	public void sendHtmlMessage(String from, String to, String subject, String text) throws Exception {	
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		
		helper.setTo(to);
		helper.setFrom(from);
		helper.setSubject(subject);
		helper.setText(text);

		try {
			sender.send(msg);
		}
		catch (Exception e){
			log.error(e.getMessage());
		}
	}
   
	/**
	 * 群发
	 * @param toArry
	 * @param subject
	 * @param text
	 * @throws Exception
	 */
    public void sendHtmlMessage(String[] toArry, String subject, String text) throws Exception {		
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		
		helper.setTo(toArry);
		helper.setFrom(from);
		helper.setSubject(subject);		
		helper.setText(text);
		try {
			sender.send(msg);
		}
		catch (Exception e){
			log.error(e.getMessage());
		}
    }

	/**
	 * Method to send email with file attachments
	 * @param to		The to address
	 * @param from		The from address
	 * @param subject	Subject of the mail
	 * @param text		Body of the mail
	 * @param filePath	Path to the file to be attached
	 * @param fileName	Name of the file to be attached
	 * @throws MessagingException
	 */
	public void sendAttachmentMessage(String[] to, String from, String subject,
			String text, String filePath, String fileName)
			throws MessagingException {
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		helper.setTo(to);
		helper.setFrom(from);
		helper.setSubject(subject);
		helper.setText(text);

		FileSystemResource img = new FileSystemResource(new File(filePath));
		helper.addAttachment(fileName, img);
		
		try {
			sender.send(msg);
		}
		catch (Exception e){
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 抄送
	 * @param from
	 * @param to
	 * @param bcc
	 * @param subject
	 * @param text
	 * @throws Exception
	 */
	public void sendHtmlMessage(String from, String to, String[] bcc, String subject, String text) throws Exception {	
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		
		helper.setTo(to);
		helper.setFrom(from);
		helper.setSubject(subject);
		helper.setText(text);
		helper.setBcc(bcc);
		
		try {
			sender.send(msg);
		}
		catch (Exception e){
			log.error(e.getMessage());
		}
}
}