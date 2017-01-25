package com.tieto.webwicker.conf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.tieto.webwicker.api.conf.Settings;

public class SettingsImpl implements Settings {
	private static final long serialVersionUID = -5200248773325425722L;
	private final Map<String,Map<String,String>> settings;
	
	public SettingsImpl() {
		settings = new HashMap<>();
	}

	public void readSettingsFromFile(String filepath) {
		File settingsFile = new File(filepath);
		if(settingsFile.canRead()) {
			try(Stream<String> stream = Files.lines(settingsFile.toPath())) {
				Iterator<String> it = stream.iterator();
				String section = "WebWicker";
				while(it.hasNext()) {
					String line = it.next().trim();
					if(line.matches("^\\[.*\\]$")) {
						section = line.substring(1, line.length()-1);
					} else if(line.matches("^.+=.+$")) {
						int splitAt = line.indexOf('=');
						putSetting(section, line.substring(0, splitAt), line.substring(splitAt+1));
					}
				}
			} catch(IOException e) {
			}			
		}
	}
	
	private void putSetting(String section, String key, String value) {
		if(!settings.containsKey(section)) {
			settings.put(section, new HashMap<>());
		}
		settings.get(section).put(key, value);
	}
	
	@Override
	public Optional<Map<String, String>> getSection(String section) {
		return Optional.ofNullable(settings.get(section));
	}

	@Override
	public Optional<String> getSetting(String section, String key) {
		return Optional.ofNullable(getSection(section).orElse(Collections.emptyMap()).get(key));
	}
}
