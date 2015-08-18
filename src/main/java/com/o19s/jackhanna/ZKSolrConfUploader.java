package com.o19s.jackhanna;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


/**
 * This class is mostly a copy of ZKController with the "final" keyword removed.
 * @author epugh
 *
 */
public class ZKSolrConfUploader {

	  //private static Logger log = LoggerFactory.getLogger(ZKSolrConfUploader.class);

	  static final String NEWL = System.getProperty("line.separator");




	  // package private for tests

//	  static final String CONFIGS_ZKNODE = "/configs";

//	  public final static String COLLECTION_PARAM_PREFIX="collection.";
//	  public final static String CONFIGNAME_PROP="configName";

	  private CuratorFramework zkClient;
	  


	  private String zkServerAddress;



	  private String hostName;
	  
	  

	  /**
	   * @param zkServerAddress ZooKeeper server host address
	   * @param zkClientTimeout
	   * @param zkClientConnectTimeout
	   * @param localHost
	   * @param locaHostPort
	   * @param localHostContext
	   * @throws InterruptedException
	   * @throws TimeoutException
	   * @throws IOException
	   */
	  public ZKSolrConfUploader(String zkServerAddress, int zkClientTimeout, int zkClientConnectTimeout) throws InterruptedException,
	      TimeoutException, IOException {
	    this.zkServerAddress = zkServerAddress;

	    RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkClientTimeout, 10, zkClientConnectTimeout);
	
		
		zkClient = CuratorFrameworkFactory.builder()
				.retryPolicy(retryPolicy)
				// TODO: support ensembleProvider via ExhibitorEnsembleProvider
				// .ensembleProvider(new ExhibitorEnsembleProvider())
				.connectString(zkServerAddress).build();
		zkClient.start();
	

	    init();
	  }

	

	  /**
	   * Closes the underlying ZooKeeper client.
	   */
	  public void close() {	
///		  if (zkClient == null){
//			  System.err.println("no client, so why are you closing?");
//		  }
//		  else {
	      zkClient.close();
//		  }
	   
	  }

	


	  
	  public String getHostName() {
	    return hostName;
	  }

	  public CuratorFramework getZkClient() {
	    return zkClient;
	  }

	  /**
	   * @return zookeeper server address
	   */
	  public String getZkServerAddress() {
	    return zkServerAddress;
	  }

	  private void init() {

	   

	  }

	 
	  

	  /**
	   * @param path
	   * @return true if the path exists
	 * @throws Exception 
	   */
	  /*
	  public boolean pathExists(String path) throws Exception {
	    return (zkClient.checkExists().forPath(path) == null);
	  }
*/


	  /**
	   * @param dir
	   * @param zkPath
	 * @throws Exception 
	   */
	  /*
	  public void uploadToZK(File dir, String zkPath) throws Exception {
	    File[] files = dir.listFiles();
	    for(File file : files) {
	      if (!file.getName().startsWith(".")) {
	        if (!file.isDirectory()) {
	        	byte[] byteArray = new byte[(int)file.length()];
	            byteArray = FileUtils.readFileToByteArray(file); 
	            String path = zkPath + "/" + file.getName();
				if (zkClient.checkExists().forPath(path) != null){
					zkClient.setData().forPath(path, byteArray);
				}
				else {
					zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, byteArray);	
				}

	        } else {
	          uploadToZK(file, zkPath + "/" + file.getName());
	        }
	      }
	    }
	  }
	  */
	  


	  // convenience for testing
	  void printLayoutToStdOut() throws Exception {
//	    zkClient.p
		  throw new Exception("Not currently supported");
	  }



}
