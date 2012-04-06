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
package org.zaproxy.zap.extension.fuzz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.owasp.jbrofuzz.core.Fuzzer;
import org.parosproxy.paros.common.ThreadPool;
import org.parosproxy.paros.network.ConnectionParam;
import org.zaproxy.zap.extension.anticsrf.AntiCsrfToken;

public class FuzzerThread implements Runnable {

    private static Logger log = Logger.getLogger(FuzzerThread.class);
	
	private ExtensionFuzz extension;
	private List<FuzzerListener> listenerList = new ArrayList<FuzzerListener>();
	private ConnectionParam connectionParam = null;
	private boolean isStop = false;
	private ThreadPool pool = null;
	private int delayInMs = 0;

	private FuzzableHttpMessage fuzzableHttpMessage;
	private Fuzzer[] fuzzers;
	private FileFuzzer[] customFuzzers;
	private AntiCsrfToken acsrfToken;
	private boolean showTokenRequests;
	private boolean followRedirects;
	private boolean urlEncode;

	private boolean pause = false;

    /**
     * 
     */
    public FuzzerThread(ExtensionFuzz extension, FuzzerParam fuzzerParam, ConnectionParam param) {
    	this.extension = extension;
	    this.connectionParam = param;
	    pool = new ThreadPool(fuzzerParam.getThreadPerScan());
	    delayInMs = fuzzerParam.getDelayInMs();
    }
    
    
    public void start() {
        isStop = false;
        Thread thread = new Thread(this, "ZAP-FuzzerThread");
        thread.setPriority(Thread.NORM_PRIORITY-2);
        thread.start();
    }
    
    public void stop() {
        isStop = true;
    }
   
	public void addFuzzerListener(FuzzerListener listener) {
		listenerList.add(listener);		
	}

	public void removeFuzzerListener(FuzzerListener listener) {
		listenerList.remove(listener);
	}

	public void notifyFuzzerComplete() {
		for (FuzzerListener listener : listenerList) {
			listener.notifyFuzzerComplete();
		}
	}


	public void setTarget(FuzzableHttpMessage fuzzableHttpMessage, Fuzzer[] fuzzers, FileFuzzer[] customFuzzers, AntiCsrfToken acsrfToken, 
			boolean showTokenRequests, boolean followRedirects, boolean urlEncode) {
		this.fuzzableHttpMessage = fuzzableHttpMessage;
		this.fuzzers = fuzzers;
		this.customFuzzers = customFuzzers;
		this.acsrfToken = acsrfToken;
		this.showTokenRequests = showTokenRequests;
		this.followRedirects = followRedirects;
		this.urlEncode = urlEncode;
	}

    public void run() {
        log.info("fuzzer started");
        
    	if (customFuzzers != null) {
    		this.fuzz(fuzzableHttpMessage, customFuzzers);
    	} else {
    		this.fuzz(fuzzableHttpMessage, fuzzers);
    	}
	    
	    pool.waitAllThreadComplete(0);
	    notifyFuzzerComplete();

        log.info("fuzzer stopped");
	}
	
	private void fuzz(FuzzableHttpMessage fuzzableHttpMessage, FileFuzzer[] customFuzzers) {
		
		int total = 0;
		for (FileFuzzer fuzzer : customFuzzers) {
			total += fuzzer.getLength();
		}
		extension.scanProgress(0, total);
		
		for (FileFuzzer fuzzer : customFuzzers) {
			Iterator<String> iter = fuzzer.getIterator();
			while (iter.hasNext()) {
				
				while (pause && ! isStop()) {
                	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// Ignore
					}
				}
				if (isStop()) {
					break;
				}
				if (delayInMs > 0) {
                	try {
						Thread.sleep(delayInMs);
					} catch (InterruptedException e) {
						// Ignore
					}
				}
				
				String fuzz = iter.next();
				FuzzProcess fp = new FuzzProcess(connectionParam, fuzzableHttpMessage, fuzz, acsrfToken, showTokenRequests, followRedirects, urlEncode);
				for (FuzzerListener listener : listenerList) {
					fp.addFuzzerListener(listener);
				}
	
				Thread thread;
	            do { 
	                thread = pool.getFreeThreadAndRun(fp);
	                if (thread == null) {
	                	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// Ignore
						}
	                }
	            } while (thread == null && !isStop());
	
			}
			if (isStop()) {
				break;
			}

		}
	}


	private void fuzz(FuzzableHttpMessage fuzzableHttpMessage, Fuzzer[] fuzzers) {
	
		int total = 0;
		for (Fuzzer fuzzer : fuzzers) {
			total += (int)fuzzer.getMaximumValue();
		}
		extension.scanProgress(0, total);
		
		for (Fuzzer fuzzer : fuzzers) {
			while (fuzzer.hasNext()) {
				
				while (pause && ! isStop()) {
                	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// Ignore
					}
				}
				if (isStop()) {
					break;
				}
				if (delayInMs > 0) {
                	try {
						Thread.sleep(delayInMs);
					} catch (InterruptedException e) {
						// Ignore
					}
				}
				
				String fuzz = fuzzer.next();
				FuzzProcess fp = new FuzzProcess(connectionParam, fuzzableHttpMessage, fuzz, acsrfToken, showTokenRequests, followRedirects, urlEncode);
				for (FuzzerListener listener : listenerList) {
					fp.addFuzzerListener(listener);
				}
	
				Thread thread;
	            do { 
	                thread = pool.getFreeThreadAndRun(fp);
	                if (thread == null) {
	                	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// Ignore
						}
	                }
	            } while (thread == null && !isStop());
	
			}
			if (isStop()) {
				break;
			}

		}
	}
	
	public boolean isStop() {
	    
	    return isStop;
	}
	
	public void pause() {
		this.pause = true;
	}
	
	public void resume () {
		this.pause = false;
	}
	
	public boolean isPaused() {
		return pause;
	}

}
