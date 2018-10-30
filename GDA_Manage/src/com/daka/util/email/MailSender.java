package com.daka.util.email;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MailSender
{

    public static String SMTP_163Mail = "smtp.163.com";

    public static String SMTP_126Mail = "smtp.126.com";

    public static String SMTP_QQMail = "smtp.qq.com";

    public static String SMTP_TomMail = "smtp.tom.com";

    public static String mailServerName = "lvbabaBus@163.com";

    public static String mailServerPassword = "xiaolvbashi2016";

    private Logger logger = Logger.getLogger(this.getClass());

    // 21-30行把本程序所用变量进行定义。 具体在main中对它们赋植。
    private MimeMessage mimeMsg; // MIME邮件对象

    private Session session; // 邮件会话对象

    private Properties props; // 系统属性

    private String username = ""; // smtp认证用户名和密码

    private String password = "";

    private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成//MimeMessage对象

    public MailSender(String smtp, String mailTheme)
    {
        setSmtpHost(smtp);
        // 设置认证
        setNeedAuth(true);

        // 创建邮件对象
        createMimeMessage();

        // 设置邮件主题
        setSubject(mailTheme);
    }

    /**
     * 设置系统属性
     * 
     * @param hostName
     */
    public void setSmtpHost(String hostName)
    {
        logger.debug("设置系统属性：mail.smtp.host = " + hostName);
        if (props == null)
        {
            props = System.getProperties(); // 获得系统属性对象
        }
        props.put("mail.smtp.host", hostName); // 设置SMTP主机
    }

    public boolean createMimeMessage()
    {
        try
        {
            logger.debug("准备获取邮件会话对象！");
            session = Session.getInstance(props, new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(mailServerName, mailServerPassword);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("获取邮件会话对象时发生错误！" + e);
            return false;
        }
        logger.debug("准备创建MIME邮件对象！");
        try
        {
            mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
            mp = new MimeMultipart(); // mp 一个multipart对象(一个存放邮件的容器)
            return true;
        }
        catch (Exception e)
        {
            logger.error("创建MIME邮件对象失败！" + e);
            return false;
        }
    }

    /**
     * @param need boolean
     * 
     */
    public void setNeedAuth(boolean need)
    {
        System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
        if (props == null)
            props = System.getProperties();
        if (need)
        {
            props.put("mail.smtp.auth", "true");
        }
        else
        {
            props.put("mail.smtp.auth", "false");
        }
    }

    /**
     * 
     * @param mailSubject String
     * @return boolean
     * 
     */
    public boolean setSubject(String mailSubject)
    {
        System.out.println("设置邮件主题！");
        try
        {
            mimeMsg.setSubject(mailSubject);
            return true;
        }
        catch (Exception e)
        {
            System.err.println("设置邮件主题发生错误！");
            return false;
        }
    }

    /**
     * 
     * @param mailBody String
     * 
     */
    public boolean setBody(String mailBody)
    {
        try
        {
            System.out.println("设置邮件体格式");
            BodyPart bp = new MimeBodyPart();
            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=utf-8>" + mailBody,
                "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            return true;
        }
        catch (Exception e)
        {
            System.err.println("设置邮件正文时发生错误！" + e);
            return false;
        }
    }

    /**
     * 
     * @param name String
     * @param pass String
     */
    public boolean addFileAffix(String filename)
    {
        System.out.println("增加邮件附件：" + filename);
        try
        {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(fileds.getName());
            mp.addBodyPart(bp);
            return true;
        }
        catch (Exception e)
        {
            System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
            return false;
        }
    }

    /**
     * @param name String
     * 
     * @param pass String
     */
    public boolean setFrom(String name, String passWord)
    {
        System.out.println("设置发信人！");
        try
        {
            username = name;
            password = passWord;
            mimeMsg.setFrom(new InternetAddress(name)); // 设置发信人
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @param name String
     * 
     * @param pass String
     * 
     */
    public boolean setTo(String to)
    {
        System.out.println("设置收信人");
        if (to == null)
            return false;
        try
        {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @param name String
     * 
     * @param pass String
     */
    public boolean setCopyTo(String copyto)
    {
        System.out.println("发送附件到");
        if (copyto == null)
            return false;
        try
        {
            mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @param name String
     * 
     * @param pass String
     * @throws MessagingException
     * 
     */
    public void sendout()
        throws MessagingException
    {
        mimeMsg.setContent(mp);
        mimeMsg.saveChanges();
        System.out.println("正在发送邮件....");
        Session mailSession = Session.getInstance(props, null);
        Transport transport = mailSession.getTransport("smtp"); // ？？？
        transport.connect((String)props.get("mail.smtp.host"), username, password);
        transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
        System.out.println("发送邮件成功！");
        transport.close();
    }

    public static void main(String[] args)
        throws MessagingException
    {
        MailSender mail = new MailSender(SMTP_163Mail, "小驴巴士注册验证码");
        mail.setFrom(mailServerName, mailServerPassword);
        mail.setTo("lvbabaBus@163.com");
        mail.setBody("<div>"
            + "<a href='http://localhost:8080/Bus/'>小驴巴士企业版</a>注册验证码：<font color='red'>541265</font></div>");
        mail.sendout();
    }
}
