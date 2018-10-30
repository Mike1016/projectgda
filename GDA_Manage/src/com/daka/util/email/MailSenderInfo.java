package com.daka.util.email;

/**   
 *鍙戦�閭欢闇�浣跨敤鐨勫熀鏈俊鎭�
 *  
 */
import java.util.Properties;

public class MailSenderInfo
{
    // 鍙戦�閭欢鐨勬湇鍔″櫒鐨処P鍜岀鍙�
    private String mailServerHost;

    private String mailServerPort = "25";

    // 閭欢鍙戦�鑰呯殑鍦板潃
    private String fromAddress;

    // 閭欢鎺ユ敹鑰呯殑鍦板潃
    private String toAddress;

    // 鐧婚檰閭欢鍙戦�鏈嶅姟鍣ㄧ殑鐢ㄦ埛鍚嶅拰瀵嗙爜
    private String userName;

    private String password;

    // 鏄惁闇�韬唤楠岃瘉
    private boolean validate = false;

    // 閭欢涓婚
    private String subject;

    // 閭欢鐨勬枃鏈唴瀹�
    private String content;

    // 閭欢闄勪欢鐨勬枃浠跺悕
    private String[] attachFileNames;

    /**
     * 鑾峰緱閭欢浼氳瘽灞炴�
     */
    public Properties getProperties()
    {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

    public String getMailServerHost()
    {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost)
    {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort()
    {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort)
    {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate()
    {
        return validate;
    }

    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }

    public String[] getAttachFileNames()
    {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] fileNames)
    {
        this.attachFileNames = fileNames;
    }

    public String getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getToAddress()
    {
        return toAddress;
    }

    public void setToAddress(String toAddress)
    {
        this.toAddress = toAddress;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String textContent)
    {
        this.content = textContent;
    }
}
