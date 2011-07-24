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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import org.parosproxy.paros.security.SslCertificateService;

/**
 * @author MaWoKi
 */
/*default*/ final class SslCertificateUtils {

	private static final long DEFAULT_VALID_DAYS = 365L;

	/**
	 * Creates a new Root CA certificate and returns private and public key as
	 * {@link KeyStore}. The {@link KeyStore#getDefaultType()} is used.
	 *
	 * @return
	 * @throws NoSuchAlgorithmException If no providers are found
	 * for 'RSA' key pair generator
	 * or 'SHA1PRNG' Secure random number generator
	 * @throws IllegalStateException in case of errors during assembling {@link KeyStore}
	 */
	public final static KeyStore createRootCA() throws NoSuchAlgorithmException {
		final Date startDate = Calendar.getInstance().getTime();
		final Date expireDate = new Date(startDate.getTime()+ (DEFAULT_VALID_DAYS * 24L * 60L * 60L * 1000L));

		final KeyPairGenerator g = KeyPairGenerator.getInstance("RSA");
		g.initialize(2048, SecureRandom.getInstance("SHA1PRNG"));
		final KeyPair keypair = g.genKeyPair();
		final PrivateKey privKey = keypair.getPrivate();
        final PublicKey  pubKey = keypair.getPublic();

		// using the hash code of the user's name and home path, keeps anonymity
		// but also gives user a chance to distinguish between each other
		X500NameBuilder namebld = new X500NameBuilder(BCStyle.INSTANCE); 
		namebld.addRDN(BCStyle.CN, "OWASP Zed Attack Proxy Root CA");
		namebld.addRDN(BCStyle.L, Integer.toHexString(System.getProperty("user.name").hashCode())
									+ Integer.toHexString(System.getProperty("user.home").hashCode()));
		namebld.addRDN(BCStyle.O, "OWASP Root CA");
		namebld.addRDN(BCStyle.OU, "OWASP ZAP Root CA");
		namebld.addRDN(BCStyle.C, "xx");
		
		X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder (
												namebld.build(),
												new BigInteger("3405691582"), // 0xCAFEBABE ;-)
												startDate,
												expireDate,
												namebld.build(),
												pubKey
											);
		
		KeyStore ks = null;
		try {
			certGen.addExtension(X509Extension.subjectKeyIdentifier, true, new SubjectKeyIdentifierStructure(pubKey));
			certGen.addExtension(X509Extension.basicConstraints, true, new BasicConstraints(true));
			certGen.addExtension(X509Extension.keyUsage, false, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment | KeyUsage.cRLSign));
			certGen.addExtension(X509Extension.extendedKeyUsage, false, new ExtendedKeyUsage(KeyPurposeId.anyExtendedKeyUsage));
			final ContentSigner sigGen = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(privKey);
			final X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(sigGen));
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, null);
			ks.setKeyEntry(SslCertificateService.ZAPROXY_JKS_ALIAS, privKey, SslCertificateService.PASSPHRASE, new Certificate[]{cert});
		} catch (final Exception e) {
			throw new IllegalStateException("Errors during assembling root CA.", e);
		}
		return ks;
	}

	/**
	 * @param keystore
	 * @return
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 */
	public final static String keyStore2String(KeyStore keystore) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		keystore.store(baos, SslCertificateService.PASSPHRASE);
		final byte[] bytes = baos.toByteArray();
		baos.close();
		return Base64.encodeBase64URLSafeString(bytes);
	}

	/**
	 * @param str
	 * @return
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 */
	public final static KeyStore String2Keystore(String str) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		final byte[] bytes = Base64.decodeBase64(str);
		final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(bais, SslCertificateService.PASSPHRASE);
		bais.close();
		return ks;
	}

}
