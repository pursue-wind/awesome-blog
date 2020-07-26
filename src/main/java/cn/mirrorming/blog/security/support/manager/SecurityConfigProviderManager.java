package cn.mirrorming.blog.security.support.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 安全配置管理器
 * 
 * @author Mireal
 */
@Component
public class SecurityConfigProviderManager {

	@Autowired
	private List<SecurityConfigProvider> securityConfigProviderList;

	// 装配所有配置提供者
	public void config(HttpSecurity http) {
		securityConfigProviderList.forEach(provider -> {
			try {
				provider.configure(http);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
