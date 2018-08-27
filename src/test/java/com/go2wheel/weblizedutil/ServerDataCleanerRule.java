package com.go2wheel.weblizedutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go2wheel.weblizedutil.util.FileUtil;

@Component
public class ServerDataCleanerRule extends ExternalResource {

	private String host;

	@Autowired
	private SettingsInDb settingsInDb;

	public ServerDataCleanerRule() {
	}

	public ServerDataCleanerRule(String host) {
		this.host = host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	protected void after() {
		if (host == null) return;
		Path serverDataRoot = settingsInDb.getDataDir().resolve(host);
		try {
			Files.list(serverDataRoot).forEach(p -> {
				try {
					FileUtil.deleteFolder(p, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
