/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2010 psiinon@gmail.com
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
package org.zaproxy.zap.extension.encoder2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.encoder.Encoder;
import org.parosproxy.paros.view.AbstractFrame;
import org.zaproxy.zap.utils.ZapTextArea;
/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class EncodeDecodeDialog extends AbstractFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(EncodeDecodeDialog.class);
	
	private JTabbedPane jTabbed = null;
	private JPanel jPanel = null;

	private ZapTextArea inputField = null;
	private ZapTextArea base64EncodeField = null;
	private ZapTextArea base64DecodeField = null;
	private ZapTextArea urlEncodeField = null;
	private ZapTextArea urlDecodeField = null;
	private ZapTextArea asciiHexEncodeField = null;
	private ZapTextArea asciiHexDecodeField = null;
	private ZapTextArea sha1HashField = null;
	private ZapTextArea md5HashField = null;
	private ZapTextArea illegalUTF82ByteField = null;
	private ZapTextArea illegalUTF83ByteField = null;
	private ZapTextArea illegalUTF84ByteField = null;

	private Encoder encoder = null;

    /**
     * @throws HeadlessException
     */
    public EncodeDecodeDialog() throws HeadlessException {
        super();
 		initialize();
    }

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setAlwaysOnTop(false);
		this.setContentPane(getJTabbed());
        this.setTitle(Constant.messages.getString("enc2.title"));
	}

	private void addField (JPanel parent, int index, JComponent c, String title) {
		final java.awt.GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = index;
		gbc.insets = new java.awt.Insets(1,1,1,1);
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 0.5D;
		gbc.weighty = 0.5D;

		final JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(c);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBorder(
				BorderFactory.createTitledBorder(
						null, title, TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
						java.awt.Color.black));

		parent.add(jsp, gbc);
	}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTabbed() {
		if (jPanel == null) {
			/*
			jPanel = new JPanel();
			jPanel.setPreferredSize(new java.awt.Dimension(800,600));

			jPanel.setLayout(new GridBagLayout());
			*/

			// jPanel is the outside one
			jPanel = new JPanel();
			jPanel.setPreferredSize(new java.awt.Dimension(800,600));
			jPanel.setLayout(new GridBagLayout());

			jTabbed = new JTabbedPane();
			jTabbed.setPreferredSize(new java.awt.Dimension(800,500));

			final JPanel jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());

			final JPanel jPanel2 = new JPanel();
			//jPanel2.setPreferredSize(new java.awt.Dimension(800,500));
			jPanel2.setLayout(new GridBagLayout());

			final JPanel jPanel3 = new JPanel();

			//jPanel3.setPreferredSize(new java.awt.Dimension(800,500));
			jPanel3.setLayout(new GridBagLayout());

			final JPanel jPanel4 = new JPanel();
			jPanel4.setLayout(new GridBagLayout());

			// 3 tabs - Encode, Decode, Hash??
			addField(jPanel1, 1, getBase64EncodeField(), Constant.messages.getString("enc2.label.b64Enc"));
			addField(jPanel1, 2, getUrlEncodeField(), Constant.messages.getString("enc2.label.urlEnc"));
			addField(jPanel1, 3, getAsciiHexEncodeField(), Constant.messages.getString("enc2.label.asciiEnc"));

			addField(jPanel2, 1, getBase64DecodeField(), Constant.messages.getString("enc2.label.b64Dec"));
			addField(jPanel2, 2, getUrlDecodeField(), Constant.messages.getString("enc2.label.urlDec"));
			addField(jPanel2, 3, getAsciiHexDecodeField(), Constant.messages.getString("enc2.label.asciiDec"));

			addField(jPanel3, 1, getSha1HashField(), Constant.messages.getString("enc2.label.sha1Hash"));
			addField(jPanel3, 2, getMd5HashField(), Constant.messages.getString("enc2.label.md5Hash"));

			addField(jPanel4, 1, getIllegalUTF82ByteField(), Constant.messages.getString("enc2.label.illegalUTF8.2byte"));
			addField(jPanel4, 2, getIllegalUTF83ByteField(), Constant.messages.getString("enc2.label.illegalUTF8.3byte"));
			addField(jPanel4, 3, getIllegalUTF84ByteField(), Constant.messages.getString("enc2.label.illegalUTF8.4byte"));




			jTabbed.addTab(Constant.messages.getString("enc2.tab.encode"), jPanel1);
			jTabbed.addTab(Constant.messages.getString("enc2.tab.decode"), jPanel2);
			jTabbed.addTab(Constant.messages.getString("enc2.tab.hash"), jPanel3);
			jTabbed.addTab(Constant.messages.getString("enc2.tab.illegalUTF8"), jPanel4);


			final java.awt.GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.gridx = 0;
			gbc1.gridy = 1;
			gbc1.insets = new java.awt.Insets(1,1,1,1);
			gbc1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gbc1.fill = java.awt.GridBagConstraints.BOTH;
			gbc1.weightx = 1.0D;
			gbc1.weighty = 0.25D;

			final java.awt.GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = 0;
			gbc2.gridy = 2;
			gbc2.insets = new java.awt.Insets(1,1,1,1);
			gbc2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gbc2.fill = java.awt.GridBagConstraints.BOTH;
			gbc2.weightx = 1.0D;
			gbc2.weighty = 1.0D;

			final JScrollPane jsp = new JScrollPane();
			jsp.setViewportView(getInputField());
			jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setBorder(
					BorderFactory.createTitledBorder(
							null, Constant.messages.getString("enc2.label.text"),
							TitledBorder.DEFAULT_JUSTIFICATION,
							javax.swing.border.TitledBorder.DEFAULT_POSITION,
							new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
							java.awt.Color.black));

			//addField(jPanel, 1, getInputField(), "Text to be encoded/decoded/hashed");
			//addField(jPanel, 2, jTabbed, "Text to be encoded/decoded/hashed");

			jPanel.add(jsp, gbc1);
			jPanel.add(jTabbed, gbc2);

			jPanel2.requestFocus();

		}
		return jPanel;
	}

	private ZapTextArea newField(boolean editable) {
		final ZapTextArea field = new ZapTextArea();
		field.setLineWrap(true);
		field.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
		field.setBorder(BorderFactory.createEtchedBorder());
		field.setEditable(editable);
		return field;
	}

	private ZapTextArea getInputField() {
		if (inputField == null) {
			inputField = newField(true);

			inputField.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					updateEncodeDecodeFields();
				}

				@Override
				public void keyTyped(KeyEvent e) {
				}

			});
		}
		return inputField;
	}

	private ZapTextArea getBase64EncodeField() {
		if (base64EncodeField == null) {
			base64EncodeField = newField(false);
		}
		return base64EncodeField;
	}

	private ZapTextArea getBase64DecodeField() {
		if (base64DecodeField == null) {
			base64DecodeField = newField(false);
		}
		return base64DecodeField;
	}

	private ZapTextArea getUrlEncodeField() {
		if (urlEncodeField == null) {
			urlEncodeField = newField(false);
		}
		return urlEncodeField;
	}

	private ZapTextArea getUrlDecodeField() {
		if (urlDecodeField == null) {
			urlDecodeField = newField(false);
		}
		return urlDecodeField;
	}

	private ZapTextArea getAsciiHexEncodeField() {
		if (asciiHexEncodeField == null) {
			asciiHexEncodeField = newField(false);
		}
		return asciiHexEncodeField;
	}

	private ZapTextArea getAsciiHexDecodeField() {
		if (asciiHexDecodeField == null) {
			asciiHexDecodeField = newField(false);
		}
		return asciiHexDecodeField;
	}

	private ZapTextArea getSha1HashField() {
		if (sha1HashField == null) {
			sha1HashField = newField(false);
		}
		return sha1HashField;
	}

	private ZapTextArea getMd5HashField() {
		if (md5HashField == null) {
			md5HashField = newField(false);
		}
		return md5HashField;
	}

	private ZapTextArea getIllegalUTF82ByteField() {
		if (illegalUTF82ByteField == null) {
			illegalUTF82ByteField = newField(false);
		}
		return illegalUTF82ByteField;
	}

	private ZapTextArea getIllegalUTF83ByteField() {
		if (illegalUTF83ByteField == null) {
			illegalUTF83ByteField = newField(false);
		}
		return illegalUTF83ByteField;
	}

	private ZapTextArea getIllegalUTF84ByteField() {
		if (illegalUTF84ByteField == null) {
			illegalUTF84ByteField = newField(false);
		}
		return illegalUTF84ByteField;
	}

	private Encoder getEncoder() {
	    if (encoder == null) {
	        encoder = new Encoder();
	    }
	    return encoder;
	}

	public String decodeHexString(String hexText) {

		String decodedText="";
		String chunk=null;

		if(hexText!=null && hexText.length()>0) {
			final int numBytes = hexText.length()/2;

			final byte[] rawToByte = new byte[numBytes];
			int offset=0;
			for(int i =0; i <numBytes; i++) {
				chunk = hexText.substring(offset,offset+2);
				offset+=2;
				rawToByte[i] = (byte) (Integer.parseInt(chunk,16) & 0x000000FF);
			}
			decodedText= new String(rawToByte);
		}
		return decodedText;
	}

	private void updateEncodeDecodeFields() {

		// Base 64
		try {
			base64EncodeField.setText(getEncoder().getBase64Encode(getInputField().getText()));
		} catch (NullPointerException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		try {
			base64DecodeField.setText(getEncoder().getBase64Decode(getInputField().getText()));
			base64DecodeField.setEnabled(base64DecodeField.getText().length() > 0);
		} catch (IOException e) {
			base64DecodeField.setText(e.getMessage());
			base64DecodeField.setEnabled(false);
		} catch (IllegalArgumentException e) {
			base64DecodeField.setText(e.getMessage());
			base64DecodeField.setEnabled(false);
		}

		// URLs
		urlEncodeField.setText(getEncoder().getURLEncode(getInputField().getText()));
		try {
			urlDecodeField.setText(getEncoder().getURLDecode(getInputField().getText()));
		} catch (final Exception e) {
			// Not unexpected
			urlDecodeField.setText("");
		}
		urlDecodeField.setEnabled(urlDecodeField.getText().length() > 0);

		// ASCII Hex
		asciiHexEncodeField.setText(
				getEncoder().getHexString(
						getInputField().getText().getBytes()));

		try {
			asciiHexDecodeField.setText(decodeHexString(getInputField().getText()));
		} catch (final Exception e) {
			// Not unexpected
			asciiHexDecodeField.setText("");
		}
		asciiHexDecodeField.setEnabled(asciiHexDecodeField.getText().length() > 0);


		// Hashes
		try {
			sha1HashField.setText(
					getEncoder().getHexString(
							getEncoder().getHashSHA1(
									getInputField().getText().getBytes())));
		} catch (final Exception e) {
			sha1HashField.setText("");
		}

		try {
			md5HashField.setText(
					getEncoder().getHexString(
							getEncoder().getHashMD5(
									getInputField().getText().getBytes())));
		} catch (final Exception e) {
			md5HashField.setText("");
		}

		//Illegal UTF8
		try {
			illegalUTF82ByteField.setText(getEncoder().getIllegalUTF8Encode(getInputField().getText(), 2));
		} catch (final Exception e) {
			// Not unexpected
			illegalUTF82ByteField.setText("");
		}

		try {
			illegalUTF83ByteField.setText(getEncoder().getIllegalUTF8Encode(getInputField().getText(), 3));
		} catch (final Exception e) {
			// Not unexpected
			illegalUTF83ByteField.setText("");
		}

		try {
			illegalUTF84ByteField.setText(getEncoder().getIllegalUTF8Encode(getInputField().getText(), 4));
		} catch (final Exception e) {
			// Not unexpected
			illegalUTF84ByteField.setText("");
		}


	}

	public void setInputField (String text) {
		this.getInputField().setText(text);
		this.updateEncodeDecodeFields();
	}

	public void updateOptions(EncodeDecodeParam options) {
		getEncoder().setBase64Charset(options.getBase64Charset());
		getEncoder().setBase64DoBreakLines(options.isBase64DoBreakLines());
		
		updateEncodeDecodeFields();
	}

}
