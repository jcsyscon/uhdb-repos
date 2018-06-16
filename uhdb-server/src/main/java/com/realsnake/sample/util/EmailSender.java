/**
 *
 */
package com.realsnake.sample.util;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * Class Name : EmailSender.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 8. 25.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 8. 25.
 * @version 1.0
 */
@Component
public class EmailSender {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 이메일(only text)을 발송한다.
     *
     * @param from
     * @param to
     * @param title
     * @param onlyText
     * @throws MessagingException
     * @throws MailException
     */
    public void sendText(final String from, final String to, final String title, final String onlyText) throws MessagingException, MailException {
        this.send(from, to, title, onlyText, false, null, null);
    }

    /**
     * 파일이 첨부된 이메일(only text)을 발송한다.
     *
     * @param from
     * @param to
     * @param title
     * @param onlyText
     * @param attachFileNames
     * @throws MessagingException
     * @throws MailException
     */
    public void sendTextWithAttach(final String from, final String to, final String title, final String onlyText, final String[] attachFileNames) throws MessagingException, MailException {
        this.send(from, to, title, onlyText, false, null, attachFileNames);
    }

    /**
     * 이메일(HTML)을 발송한다.
     *
     * @param from
     * @param to
     * @param title
     * @param html
     * @throws MessagingException
     * @throws MailException
     */
    public void sendHtml(final String from, final String to, final String title, final String html) throws MessagingException, MailException {
        this.send(from, to, title, html, true, null, null);
    }

    /**
     * 본문에 이미지 파일이 삽입된 이메일(HTML)을 발송한다.
     *
     * @param from
     * @param to
     * @param title
     * @param html
     * @param inlineMap
     * @throws MessagingException
     * @throws MailException
     */
    public void sendHtmlWithInline(final String from, final String to, final String title, final String html, Map<String, String> inlineMap) throws MessagingException, MailException {
        this.send(from, to, title, html, true, inlineMap, null);
    }

    /**
     * 파일이 첨부된 이메일(HTML)을 발송한다.
     *
     * @param from
     * @param to
     * @param title
     * @param html
     * @param attachFileNames
     * @throws MessagingException
     * @throws MailException
     */
    public void sendHtmlWithAttach(final String from, final String to, final String title, final String html, String[] attachFileNames) throws MessagingException, MailException {
        this.send(from, to, title, html, true, null, attachFileNames);
    }

    /**
     * 본문에 이미지 파일이 삽입되고 파일이 첨부된 이메일(HTML)을 발송한다.
     *
     * @param from
     * @param to
     * @param title
     * @param html
     * @param inlineMap
     * @param attachFileNames
     * @throws MessagingException
     * @throws MailException
     */
    public void sendHtmlWithInlineAndAttach(final String from, final String to, final String title, final String html, Map<String, String> inlineMap, String[] attachFileNames)
            throws MessagingException, MailException {
        this.send(from, to, title, html, true, inlineMap, attachFileNames);
    }

    /**
     * 이메일을 발송한다.
     *
     * @param from
     * @param to
     * @param title
     * @param body
     * @param isHtml
     * @param inlineMap
     * @param attachFileNames
     * @throws MessagingException
     * @throws MailException
     */
    private void send(final String from, final String to, final String title, final String body, boolean isHtml, Map<String, String> inlineMap, String[] attachFileNames)
            throws MessagingException, MailException {

        /* @formatter:off */
//        logger.debug("<<username: {}, password: {}>>", this.javaMailSender.getUsername(), this.javaMailSender.getPassword());
//
//        Properties props = this.javaMailSender.getJavaMailProperties();
//
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(javaMailSender.getUsername(), "");
//            }
//        });
//
//        final MimeMessage mimeMessage = new MimeMessage(session);
        /* @formatter:on */

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(body, isHtml);

        if (isHtml) {
            // helper.setText("<html><body><img src='cid:identifier01'></body></html>", true);
            // FileSystemResource res = new FileSystemResource(new File("c:/Sample.jpg"));
            // helper.addInline("identifier01", res); // 특정 Content-ID로(이 예제에서는 identifier01) mime 메시지에 내장 리소스를 추가한다

            if (inlineMap != null && !inlineMap.isEmpty()) {
                for (String contentId : inlineMap.keySet()) {
                    String inlineFileName = inlineMap.get(contentId);

                    if (StringUtils.isEmpty(inlineFileName)) {
                        continue;
                    }

                    FileSystemResource fsr = new FileSystemResource(new File(inlineFileName));
                    message.addInline(contentId, fsr);
                }
            }
        }

        if (attachFileNames != null && attachFileNames.length > 0) {
            for (String attachFileName : attachFileNames) {
                if (StringUtils.isEmpty(attachFileName)) {
                    continue;
                }

                FileSystemResource fsr = new FileSystemResource(new File(attachFileName));
                message.addAttachment(fsr.getFilename(), fsr);
            }
        }

        this.mailSender.send(mimeMessage);

        logger.debug("<<이메일 발송 성공>> 보낸사람: {}, 받는 사람: {}, 제목: {}, 본문: {}", from, to, title, body);
    }

    /* @formatter:off */
    /**
    public void init() {

    }
    */
    /* @formatter:on */

}
