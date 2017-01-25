package com.tieto.webwicker.api.conf;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface Settings extends Serializable {
	Optional<Map<String, String>> getSection(String section);
	Optional<String> getSetting(String section, String key);
}
