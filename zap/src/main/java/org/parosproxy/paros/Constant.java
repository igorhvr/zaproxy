/*
 * Created on May 18, 2004
 *
 * Paros and its related class files.
 * 
 * Paros is an HTTP/HTTPS proxy for assessing web application security.
 * Copyright (C) 2003-2005 Chinotec Technologies Company
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Clarified Artistic License
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Clarified Artistic License for more details.
 * 
 * You should have received a copy of the Clarified Artistic License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
// ZAP: 2011/08/03 Revamped upgrade for 1.3.2
// ZAP: 2011/10/05 Write backup file to user dir
// ZAP: 2011/11/15 Changed to use ZapXmlConfiguration, to enforce the same character encoding when reading/writing configurations.
//      Changed to use the correct file when an error occurs during the load of the configuration file.
//      Removed the calls XMLConfiguration.load() as they are not needed, the XMLConfiguration constructor used already does that.
// ZAP: 2011/11/20 Support for extension factory
// ZAP: 2012/03/03 Added ZAP homepage
// ZAP: 2012/03/15 Removed a @SuppressWarnings annotation from the method copyAllProperties.
// ZAP: 2012/03/17 Issue 282 ZAP and PAROS team constants

package org.parosproxy.paros;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.parosproxy.paros.extension.option.OptionsParamView;
import org.parosproxy.paros.model.FileCopier;
import org.zaproxy.zap.utils.ZapXmlConfiguration;

/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class Constant {
	// ZAP: rebrand
    public static final String PROGRAM_NAME     = "OWASP ZAP";
    public static final String PROGRAM_NAME_SHORT = "ZAP";
    public static final String ZAP_HOMEPAGE		= "http://www.owasp.org/index.php/ZAP";
    public static final String ZAP_EXTENSIONS_PAGE		= "https://code.google.com/p/zap-extensions/";
    public static final String ZAP_TEAM			= "ZAP Dev Team";
    public static final String PAROS_TEAM		= "Chinotec Technologies";
    
//  ************************************************************
//  the config.xml MUST be set to be the same as the version_tag
//  otherwise the config.xml will be overwritten everytime.
//  ************************************************************
    public static final String DEV_VERSION = "Dev Build";
    public static final String ALPHA_VERSION = "alpha";
    public static final String BETA_VERSION = "beta";
    // Note: Change this before building a release!
    public static final String PROGRAM_VERSION = "1.4." + ALPHA_VERSION + ".1";
    //public static final String PROGRAM_VERSION = DEV_VERSION;
    
    private static final long VERSION_TAG = 1003003;
    
    // Old version numbers - for upgrade
	private static final long V_1_3_1_TAG = 1003001;
	private static final long V_1_3_0_TAG = 1003000;
    private static final long V_1_2_1_TAG = 1002001;
    private static final long V_1_2_0_TAG = 1002000;
    private static final long V_1_1_0_TAG = 1001000;
    private static final long V_1_0_0_TAG = 1000000;
    private static final long V_PAROS_TAG = 30020013;
    
//  ************************************************************
//  note the above
//  ************************************************************
    
    public static final String PROGRAM_TITLE = PROGRAM_NAME + " " + PROGRAM_VERSION;
    public static final String SYSTEM_PAROS_USER_LOG = "zap.user.log";
    
//  public static final String FILE_CONFIG = "xml/config.xml";
//  public static final String FOLDER_PLUGIN = "plugin";
//  public static final String FOLDER_FILTER = "filter";
//  public static final String FOLDER_SESSION = "session";
//  public static final String DBNAME_TEMPLATE = "db/parosdb";
//  public static final String DBNAME_UNTITLED = FOLDER_SESSION + "/untitled";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    public static final String FILE_CONFIG_DEFAULT = "xml/config.xml";
    public static final String FILE_CONFIG_NAME = "config.xml";
    public static final String FOLDER_PLUGIN = "plugin";
    public static final String FOLDER_FILTER = "filter";
    public static final String FOLDER_SESSION_DEFAULT = "session";
    public static final String DBNAME_TEMPLATE = "db" + System.getProperty("file.separator") + "zapdb";
    public static final String MESSAGES_PREFIX = "Messages";

    public static final String DBNAME_UNTITLED_DEFAULT = FOLDER_SESSION_DEFAULT + System.getProperty("file.separator") + "untitled";

    public String FILE_CONFIG = FILE_CONFIG_NAME;
    public String FOLDER_SESSION = "session";
    public String DBNAME_UNTITLED = FOLDER_SESSION + System.getProperty("file.separator") + "untitled";
    public String ACCEPTED_LICENSE_DEFAULT = "AcceptedLicense";
    public String ACCEPTED_LICENSE = ACCEPTED_LICENSE_DEFAULT;
    
    public static final String FILE_PROGRAM_SPLASH = "resource/zap128x128.png";
    
	// Accelerator keys - Default: Windows
	public static String ACCELERATOR_UNDO = "control Z";
	public static String ACCELERATOR_REDO = "control Y";
	public static String ACCELERATOR_TRIGGER_KEY = "Control";
    
    private static Constant instance = null;
    
    public static final int MAX_HOST_CONNECTION = 10;
    // ZAP: Dont announce ourselves
    //public static final String USER_AGENT = PROGRAM_NAME + "/" + PROGRAM_VERSION;
    public static final String USER_AGENT = "";

    private static String staticEyeCatcher = "0W45pz4p";
    private static boolean staticSP = false;
    
    private static String zapHome = null;

    
    // ZAP: Added i18n
    public static ResourceBundle messages = null;
    public static Locale locale = null;

    // ZAP: Added vulnerabilities file
    public String VULNS_CONFIG = "xml/vulnerabilities.xml";
    
    // ZAP: Added dirbuster dir
    public String DIRBUSTER_DIR = "dirbuster";
    public String DIRBUSTER_CUSTOM_DIR = DIRBUSTER_DIR;

    public String FUZZER_DIR = "fuzzers";
    public String FUZZER_CUSTOM_DIR = FUZZER_DIR;

	public static URL OK_FLAG_IMAGE_URL = Constant.class.getResource("/resource/icon/10/072.png"); 		// Green
	public static URL INFO_FLAG_IMAGE_URL = Constant.class.getResource("/resource/icon/10/073.png"); 	// Blue
	public static URL LOW_FLAG_IMAGE_URL = Constant.class.getResource("/resource/icon/10/074.png");		// Yellow
	public static URL MED_FLAG_IMAGE_URL = Constant.class.getResource("/resource/icon/10/076.png");		// Orange
	public static URL HIGH_FLAG_IMAGE_URL = Constant.class.getResource("/resource/icon/10/071.png");	// Red
	public static URL BLANK_IMAGE_URL = Constant.class.getResource("/resource/icon/10/blank.png");

    public static String getEyeCatcher() {
        return staticEyeCatcher;
    }
    
    public static void setEyeCatcher(String eyeCatcher) {
        staticEyeCatcher = eyeCatcher;
    }
    
    public static void setSP(boolean isSP) {
        staticSP = isSP;
    }

    public static boolean isSP() {
        return staticSP;
    }


    public Constant() {
    	initializeFilesAndDirectories();
    	setAcceleratorKeys();
    }
    	
    private void initializeFilesAndDirectories() {
        
    	FileCopier copier = new FileCopier();
        File f = null;
        Logger log = null;

        // default to use application directory 'log'
        System.setProperty(SYSTEM_PAROS_USER_LOG, "log");

        if (zapHome == null) {
            zapHome = System.getProperty("user.home");
            if (zapHome == null) {
            	zapHome = ".";
            }

            if (isLinux()) {
            	// Linux: Hidden Zap directory in the user's home directory
            	zapHome += FILE_SEPARATOR + "." + PROGRAM_NAME_SHORT;
			} else if (isMacOsX()) {
				// Mac Os X: Support for writing the configuration into the users Library 
				zapHome += FILE_SEPARATOR + "Library" + FILE_SEPARATOR
					+ "Application Support" + FILE_SEPARATOR + PROGRAM_NAME_SHORT;
			} else {
				// Windows: Zap directory in the user's home directory
				zapHome += FILE_SEPARATOR + PROGRAM_NAME;
			}

        }
		
		f = new File(zapHome);
		zapHome += FILE_SEPARATOR;
		FILE_CONFIG = zapHome + FILE_CONFIG;
		FOLDER_SESSION = zapHome + FOLDER_SESSION;
		DBNAME_UNTITLED = zapHome + DBNAME_UNTITLED;
		ACCEPTED_LICENSE = zapHome + ACCEPTED_LICENSE;
		DIRBUSTER_CUSTOM_DIR = zapHome + DIRBUSTER_DIR;
		FUZZER_CUSTOM_DIR = zapHome + FUZZER_CUSTOM_DIR;

        try {
            System.setProperty(SYSTEM_PAROS_USER_LOG, zapHome);
            
            if (!f.isDirectory()) {
                if (! f.mkdir() ) {
                	// ZAP: report failure to create directory
                	System.out.println("Failed to create directory " + f.getAbsolutePath());
                }
            }
            
            // Setup the logging
            File logFile = new File(zapHome + "/log4j.properties");
            if (!logFile.exists()) {
            	copier.copy(new File("xml/log4j.properties"),logFile);
            }
            System.setProperty("log4j.configuration", logFile.getAbsolutePath());
            PropertyConfigurator.configure(logFile.getAbsolutePath());
            log = Logger.getLogger(Constant.class);
            
            f = new File(FILE_CONFIG);
            if (!f.isFile()) {
            	// try old location
            	File oldf = new File (zapHome + FILE_SEPARATOR + "zap" + FILE_SEPARATOR + FILE_CONFIG_NAME);
            	
            	if (oldf.exists()) {
            		log.info("Copying defaults from " + oldf.getAbsolutePath() + " to " + FILE_CONFIG);
            		copier.copy(oldf,f);
            		
            	} else {
            		log.info("Copying defaults from " + FILE_CONFIG_DEFAULT+" to " + FILE_CONFIG);
            		copier.copy(new File(FILE_CONFIG_DEFAULT),f);
            	}
            }
            
            f=new File(FOLDER_SESSION);
            if (!f.isDirectory()) {
                log.info("Creating directory "+FOLDER_SESSION);
                if (! f.mkdir() ) {
                	// ZAP: report failure to create directory
                	System.out.println("Failed to create directory " + f.getAbsolutePath());
                }
            }
            f = new File(DIRBUSTER_CUSTOM_DIR);
            if (!f.isDirectory()) {
                log.info("Creating directory " + DIRBUSTER_CUSTOM_DIR);
                if (! f.mkdir() ) {
                	// ZAP: report failure to create directory
                	System.out.println("Failed to create directory " + f.getAbsolutePath());
                }
            }
            f = new File(FUZZER_CUSTOM_DIR);
            if (!f.isDirectory()) {
                log.info("Creating directory " + FUZZER_CUSTOM_DIR);
                if (! f.mkdir() ) {
                	// ZAP: report failure to create directory
                	System.out.println("Failed to create directory " + f.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            System.err.println("Unable to initialize home directory! " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
        
        
        // Upgrade actions
        try {
	        try {
	            
	            // ZAP: Changed to use ZapXmlConfiguration, to enforce the same character encoding when reading/writing configurations.
	            XMLConfiguration config = new ZapXmlConfiguration(FILE_CONFIG);
	            config.setAutoSave(false);
	
	            long ver = config.getLong("version");
	            
	            if (ver == VERSION_TAG) {
	            	// Nothing to do
	            } else if (PROGRAM_VERSION.equals(DEV_VERSION)) {
	            	// Nothing to do
	            } else {
	            	// Backup the old one
	            	log.info("Backing up config file to " + FILE_CONFIG + ".bak");
            		f = new File(FILE_CONFIG);
	                try {
						copier.copy(f, new File(FILE_CONFIG + ".bak"));
					} catch (IOException e) {
						String msg = "Failed to backup config file " + 
	            			FILE_CONFIG + " to " + FILE_CONFIG + ".bak " + e.getMessage();
			            System.err.println(msg);
			            log.error(msg, e);
					}
	                
		            if (ver == V_PAROS_TAG) {
	            		upgradeFrom1_1_0(config);
	            		upgradeFrom1_2_0(config);
		            }
	            	if (ver <= V_1_0_0_TAG) {
	            		// Nothing to do
	            	}
	            	if (ver <= V_1_1_0_TAG) {
	            		upgradeFrom1_1_0(config);
	            	}
	            	if (ver <= V_1_2_0_TAG) {
	            		upgradeFrom1_2_0(config);
	            	}
	            	if (ver <= V_1_2_1_TAG) {
	            		// Nothing to do
	            	}
	            	if (ver <= V_1_3_0_TAG) {
	            		// Nothing to do
	            	}
	            	if (ver <= V_1_3_1_TAG) {
	            		// Nothing to do
	            	}
	            	log.info("Upgraded from " + ver);
            		
            		// Update the version
            		config.setProperty("version", VERSION_TAG);
            		config.save();
            	}

	        } catch (ConfigurationException e) {
	            //  if there is any error in config file (eg config file not exist),
	            //  overwrite previous configuration file 
	            // ZAP: changed to use the correct file
	            copier.copy(new File(FILE_CONFIG_DEFAULT), new File(FILE_CONFIG));
	
	        } catch (NoSuchElementException e) {
	            //  overwrite previous configuration file if config file corrupted
	            // ZAP: changed to use the correct file
	            copier.copy(new File(FILE_CONFIG_DEFAULT), new File(FILE_CONFIG));
	            
	        }
        } catch (Exception e) {
            System.err.println("Unable to upgrade config file " + FILE_CONFIG + " " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
        
        // ZAP: Init i18n
        
        String lang = null;
        locale = Locale.ENGLISH;
        try {
            // Select the correct locale
            // ZAP: Changed to use ZapXmlConfiguration, to enforce the same character encoding when reading/writing configurations.
            XMLConfiguration config = new ZapXmlConfiguration(FILE_CONFIG);
            config.setAutoSave(false);

            lang = config.getString(OptionsParamView.LOCALE, OptionsParamView.DEFAULT_LOCALE);
            if (lang.length() == 0) {
            	lang = OptionsParamView.DEFAULT_LOCALE;
            }
            String[] langArray = lang.split("_");
            locale = new Locale(langArray[0], langArray[1]);
        } catch (Exception e) {
        	System.out.println("Failed to initialise locale " + e);
        }
        
	    messages = ResourceBundle.getBundle(MESSAGES_PREFIX, locale);
    }
    
    private void copyProperty(XMLConfiguration fromConfig, XMLConfiguration toConfig, String key) {
    	toConfig.setProperty(key, fromConfig.getProperty(key));
    }
    
	private void copyAllProperties(XMLConfiguration fromConfig, XMLConfiguration toConfig, String prefix) {
    	Iterator<?> iter = fromConfig.getKeys(prefix);
    	while (iter.hasNext()) {
    		String key = (String)iter.next();
    		copyProperty(fromConfig, toConfig, key);
    	}
    }
    
    private void upgradeFrom1_1_0(XMLConfiguration config) throws ConfigurationException {
		// Upgrade the regexs
        // ZAP: Changed to use ZapXmlConfiguration, to enforce the same character encoding when reading/writing configurations.
        XMLConfiguration newConfig = new ZapXmlConfiguration(FILE_CONFIG_DEFAULT);
        newConfig.setAutoSave(false);

        copyAllProperties(newConfig, config, "pscans");                
	}
    
    private void upgradeFrom1_2_0(XMLConfiguration config) throws ConfigurationException {
		// Upgrade the regexs
        // ZAP: Changed to use ZapXmlConfiguration, to enforce the same character encoding when reading/writing configurations.
        XMLConfiguration newConfig = new ZapXmlConfiguration(FILE_CONFIG_DEFAULT);
        newConfig.setAutoSave(false);

        copyProperty(newConfig, config, "view.editorView");
        copyProperty(newConfig, config, "view.brkPanelView");
        copyProperty(newConfig, config, "view.showMainToolbar");
	}

	public static void setLocale (String loc) {
        String[] langArray = loc.split("_");
        locale = new Locale(langArray[0], langArray[1]);
	    messages = ResourceBundle.getBundle(MESSAGES_PREFIX, locale);
    }
	
	public static Locale getLocale () {
		return locale;
	}
    
    public static Constant getInstance() {
        if (instance==null) {
            instance=new Constant();
        }
        return instance;

    }
    
    private void setAcceleratorKeys() {

		// Undo/Redo
		if (Constant.isMacOsX()) {
			ACCELERATOR_UNDO = "meta Z";
			ACCELERATOR_REDO = "meta shift Z";
			ACCELERATOR_TRIGGER_KEY = "Meta";
		} else {
			ACCELERATOR_UNDO = "control Z";
			ACCELERATOR_REDO = "control Y";
			ACCELERATOR_TRIGGER_KEY = "Control";
		}
	}
    
    
    // Determine Windows Operating System
    private static Pattern patternWindows = Pattern.compile("window", Pattern.CASE_INSENSITIVE);
    
    public static boolean isWindows() {
        String os_name = System.getProperty("os.name");
        
        Matcher matcher = patternWindows.matcher(os_name);
        return matcher.find();
    }
    
    // Determine Linux Operating System
    private static Pattern patternLinux = Pattern.compile("linux", Pattern.CASE_INSENSITIVE);
    
    public static boolean isLinux() {
        String os_name = System.getProperty("os.name");
        Matcher matcher = patternLinux.matcher(os_name);
        return matcher.find();
    }
    
    // Determine Windows Operating System
    private static Pattern patternMacOsX = Pattern.compile("mac", Pattern.CASE_INSENSITIVE);
    
    public static boolean isMacOsX() {
        String os_name = System.getProperty("os.name");
        Matcher matcher = patternMacOsX.matcher(os_name);
        return matcher.find();
    }
    
    public static void setZapHome (String dir) {
    	zapHome = dir;
    }
    
}
