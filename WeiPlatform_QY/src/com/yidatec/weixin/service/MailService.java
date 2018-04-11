package com.yidatec.weixin.service;


import java.io.File;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.yidatec.weixin.entity.MailEntity;
import com.yidatec.weixin.util.GetAndReadAllFile;
import com.yidatec.weixin.util.PropertiesUtil;


/**
 * 系统发送mail用
 *
 */
public class MailService {

    /**
     * 日志记录
     */
    private static final Logger log = LogManager.getLogger(MailService.class);

    /**
     * mail发送方
     */
    private String from;

    /**
     * mail发送执行
     */
    private JavaMailSender sender;

    /**
     * 帐号mail的标题
     */
    private String accouontSubject;

    /**
     * 帐号mail的内容
     */
    private String accountMailContent;

    /**
     * 找回密码mail的标题
     */
    private String retrievePasswordSubject;

    /**
     * 找回密码mail的内容
     */
    private String retrievePasswordContent;

    /**
     * mail内容中用户名变量
     */
    public static final String VAR_USER_NAME = "%username%";

    /**
     * mail内容中用户名变量
     */
    public static final String VAR_PASSWORD = "%password%";




    /**
     * 发送邮件
     *
     * @param mail 邮件定义
     * @throws Exception 任何错误发生将抛出异常
     */
    @SuppressWarnings("static-access")
	public void sendMail(MailEntity mail) throws Exception {
        try {
            MimeMessage msg = getSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
            // 是否有发送方
            if (mail.hasFrom())
                helper.setFrom(mail.getFrom());
            else
                // 没有就使用默认的
                helper.setFrom(from);

            helper.setTo(mail.getTo());


            // 是否有抄送
            if (mail.hasCc())
                helper.setCc(mail.getCc());

            if (mail.hasBcc())
                helper.setBcc(mail.getBcc());

            if (mail.hasAttchments())
                for (int i = 0; i < mail.getAttachments().length; i++) {
                    helper.addAttachment(mail.getAttchmentNames()[i], mail.getAttachments()[i]);
                }

            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), mail.isHtml());
            
            //log附件
           /* String csv_path = PropertiesUtil.ppsConfig.getProperty("CSV_OLDPATH");
			File csvormd5file=new File(csv_path);
			GetAndReadAllFile getAndReadAllFile = new GetAndReadAllFile();
			List<String> fileList_log = getAndReadAllFile.getFileList(csvormd5file, "log");
			if(fileList_log.size()>0){
				File file_log=new File(fileList_log.get(0).toString());
	            //是否有log附件
	            if(file_log.isFile() && file_log.exists()){
	            	//附件名
	                String attchments = fileList_log.get(0).toString();
	                //增加附件
	                FileSystemResource file = new FileSystemResource(attchments);  
	                helper.addInline("file", file);  
	                helper.addAttachment(MimeUtility.encodeWord(file_log.getName()), file_log); 
	            }
			}*/
            asyncSendMail(msg);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 异步发送邮件
     *
     * @param msg 邮件消息定义
     * @throws Exception 任何错误发生将抛出异常
     */
    private void asyncSendMail(final MimeMessage msg) throws Exception {
        new Runnable() {

            @Override
            public void run() {
                getSender().send(msg);
            }
        }.run();
    }

    /**
     * 获取邮件发送者
     *
     * @return 邮件发送者
     */
    public JavaMailSender getSender() {
        return sender;
    }

    /**
     * 设置邮件发送者
     *
     * @param sender 待设置的值
     */
    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    /**
     * 获取邮件发送人的mail地址
     *
     * @return String mail地址
     */
    public String getFrom() {
        return from;
    }

    /**
     * 获取邮件发送人的mail地址
     *
     * @param from 待设置mail地址
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 获取帐号mail的内容
     *
     * @return String
     */
    public String getAccountMailContent() {
        return accountMailContent;
    }

    /**
     * 设置帐号mail的内容
     *
     * @param accountMailContent 待设置的值
     */
    public void setAccountMailContent(String accountMailContent) {
        this.accountMailContent = accountMailContent;
    }

    /**
     * 获取找回密码mail的内容
     *
     * @return String
     */
    public String getRetrievePasswordContent() {
        return retrievePasswordContent;
    }

    /**
     * 设置找回密码mail的内容
     *
     * @param retrievePasswordContent 待设置的值
     */
    public void setRetrievePasswordContent(String retrievePasswordContent) {
        this.retrievePasswordContent = retrievePasswordContent;
    }

    /**
     * 获取帐号mail主题
     *
     * @return String
     */
    public String getAccouontSubject() {
        return accouontSubject;
    }

    /**
     * 设置帐号mail主题
     *
     * @param accouontSubject 待设置的值
     */
    public void setAccouontSubject(String accouontSubject) {
        this.accouontSubject = accouontSubject;
    }

    /**
     * 获取找回密码mail主题
     *
     * @return String
     */
    public String getRetrievePasswordSubject() {
        return retrievePasswordSubject;
    }

    /**
     * 设置找回密码mail主题
     *
     * @param retrievePasswordSubject 待设置的值
     */
    public void setRetrievePasswordSubject(String retrievePasswordSubject) {
        this.retrievePasswordSubject = retrievePasswordSubject;
    }
}