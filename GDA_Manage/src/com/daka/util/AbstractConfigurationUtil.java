package com.daka.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * 配置文件工具父类
 * 
 * @author 刘新
 *
 */   
@Component
public abstract class AbstractConfigurationUtil {

	protected Properties conf = new Properties();

	/**
	 * 获取配置信息
	 * 
	 * @param key
	 * @return
	 */
	public String getConfigValue(String key) {
		if (StringUtils.isEmpty(key)) {
			return StringUtils.EMPTY;
		}

		if (conf.isEmpty()) {
			try {
				readConfigurationFile();
			} catch (Exception e) {
				getLogger().error(e.getLocalizedMessage());
				return StringUtils.EMPTY;
			}
		}

		return conf.getProperty(key, StringUtils.EMPTY);
	}

	/**
	 * 读取配置文件
	 */
	protected void readConfigurationFile() throws Exception {
		InputStream inputStream = null;
		ClassPathResource resource = new ClassPathResource(
				getConfurationFilePath());
		inputStream = resource.getInputStream();
		conf.load(inputStream);

		if (inputStream != null) {
			inputStream.close();
		}
	}

	/**
	 * 返回�?��配置信息键�?
	 * 
	 * @return
	 */
	public String[] getConfigurationKeys() {
		if (conf.isEmpty()) {
			try {
				readConfigurationFile();
			} catch (Exception e) {
				getLogger().error(e.getLocalizedMessage());
				return new String[0];
			}
		}

		return conf.keySet().toArray(new String[conf.size()]);
	}

	/**
	 * 获取配置文件路径
	 * 
	 * @return
	 */
	protected abstract String getConfurationFilePath();

	/**
	 * 获取日志�?
	 * 
	 * @return
	 */
	protected abstract Logger getLogger();

}
