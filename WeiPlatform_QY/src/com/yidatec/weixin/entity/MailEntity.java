package com.yidatec.weixin.entity;

import org.springframework.core.io.InputStreamSource;

/**
 * 邮件信息
 *
 * @author QuShengWen
 */
public class MailEntity {

    /**
     * 邮件发送者mail地址
     */
    private String from = null;

    /**
     * 邮件接收者mail地址列表
     */
    private String[] to = null;

    /**
     * 抄送接收者mail地址列表
     */
    private String[] cc = null;

    /**
     * 暗送接收者mail地址列表
     */
    private String[] bcc = null;

    /**
     * 邮件标题
     */
    private String subject = null;

    /**
     * 邮件内容
     */
    private String content = null;

    /**
     * 邮件是否为html格式
     */
    private boolean isHtml = true;

    /**
     * 附件名字列表
     */
    private String[] attchmentNames;

    /**
     * 附件内容列表
     */
    private InputStreamSource[] attachments;

    /**
     * 是否有发送者mail
     *
     * @return boolean true 有发送者；false 无发送者
     */
    public boolean hasFrom() {
        return getFrom() != null && !getFrom().isEmpty();
    }

    /**
     * 是否有接收者
     *
     * @return boolean true 有接收者；false 无接收者
     */
    public boolean hasTo() {
        return getTo() != null && getTo().length > 0;
    }

    /**
     * 是否有抄送着
     *
     * @return boolean true 有抄送者；false 无抄送者
     */
    public boolean hasCc() {
        return getCc() != null && getCc().length > 0;
    }

    /**
     * 是否有暗送着
     *
     * @return boolean true 有暗送者；false 无暗送者
     */
    public boolean hasBcc() {
        return getBcc() != null && getBcc().length > 0;
    }

    /**
     * 是否有附件
     *
     * @return boolean true 有附件；false 无附件
     */
    public boolean hasAttchments() {
        return getAttachments() != null && getAttachments().length > 0;
    }

    /**
     * 获取邮件发送者mail地址
     *
     * @return String
     */
    public String getFrom() {
        return from;
    }

    /**
     * 设置邮件发送者mail地址
     *
     * @param from 待设置的值
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 获取邮件接收者mail地址
     *
     * @return String
     */
    public String[] getTo() {
        return to;
    }

    /**
     * 设置邮件接收者mail地址
     *
     * @param to 待设置的值
     */
    public void setTo(String[] to) {
        this.to = to;
    }

    /**
     * 获取邮件抄送者mail地址数组
     *
     * @return 邮件抄送者mail地址数组
     */
    public String[] getCc() {
        return cc;
    }

    /**
     * 设置邮件抄送者mail地址数组
     *
     * @param cc 待设置的值
     */
    public void setCc(String[] cc) {
        this.cc = cc;
    }

    /**
     * 获取邮件暗送者mail地址数组
     *
     * @return 邮件暗送者mail地址数组
     */
    public String[] getBcc() {
        return bcc;
    }

    /**
     * 设置邮件暗送者mail地址数组
     *
     * @param bcc 待设置的值
     */
    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    /**
     * 获取邮件标题
     *
     * @return String
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置邮件标题
     *
     * @param subject 待设置的值
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取邮件内容
     *
     * @return String
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置邮件内容
     *
     * @param content 待设置的值
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 判断是否为html内容
     *
     * @return boolean true 是html；false 非html
     */
    public boolean isHtml() {
        return isHtml;
    }

    /**
     * 设置邮件内容为html
     *
     * @param html 待设置的值
     */
    public void setHtml(boolean html) {
        isHtml = html;
    }

    /**
     * 获取附件列表
     *
     * @return 附件列表
     */
    public InputStreamSource[] getAttachments() {
        return attachments;
    }

    /**
     * 设置附件列表
     *
     * @param attachments
     */
    public void setAttachments(InputStreamSource[] attachments) {
        this.attachments = attachments;
    }

    /**
     * 获取附件名列表
     *
     * @return 附件名列表
     */
    public String[] getAttchmentNames() {
        return attchmentNames;
    }

    /**
     * 设置附件名列表
     *
     * @param attchmentNames
     */
    public void setAttchmentNames(String[] attchmentNames) {
        this.attchmentNames = attchmentNames;
    }
}
