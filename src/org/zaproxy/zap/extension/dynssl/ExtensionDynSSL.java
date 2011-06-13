/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2011 mawoki@ymail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.dynssl;

import java.security.KeyStore;

import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.security.SslCertificateService;
import org.parosproxy.paros.security.SslCertificateServiceImpl;

/**
 * Extension enables configuration for Root CA certificate
 *
 * @author MaWoKi
 */
public class ExtensionDynSSL extends ExtensionAdaptor {

	public static final String EXTENSION_ID = "ExtensionDynSSL";
	
	private DynSSLParam params;
	private DynamicSSLPanel optionsPanel;

	private final Logger logger = Logger.getLogger(ExtensionDynSSL.class);

	public ExtensionDynSSL() {
		super();
		this.setName(EXTENSION_ID);
	}

	@Override
	public void hook(ExtensionHook extensionHook) {
		super.hook(extensionHook);
	    if (getView() != null) {
	        extensionHook.getHookView().addOptionPanel(getOptionsPanel());
	        extensionHook.addOptionsParamSet(getParams());
	    }
	}

	@Override
	public void start() {
	    final SslCertificateService service = SslCertificateServiceImpl.getService();
	    try {
			final KeyStore rootca = getParams().getRootca();
			service.initializeRootCA(rootca);
		} catch (final Exception e) {
			logger.error("Couldn't initialize Root CA", e);
		}
	}

	private DynamicSSLPanel getOptionsPanel() {
		if (optionsPanel == null) {
			optionsPanel = new DynamicSSLPanel();
		}
		return optionsPanel;
	}

	public DynSSLParam getParams() {
		if (params == null) {
			params = new DynSSLParam();
		}
		return params;
	}

}
