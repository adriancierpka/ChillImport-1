package com.chillimport.server;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

public final class FrostSetup {
	private static boolean ready = false;
	private static GenericContainer database;
	private static GenericContainer frostserver;
	private static Network network;
	
	public synchronized static String getFrostURL() {
		if (!ready) {
			network = Network.newNetwork();
	    	//create FrostServer with database
	    	database = new GenericContainer("mdillon/postgis:10")
					.withEnv("POSTGRES_DB","sensorthings")
					.withEnv("POSTGRES_USER","sensorthings")
					.withEnv("POSTGRES_PASSWORD","ChangeMe")
					.withNetwork(network)
					.withNetworkAliases("database");
	    	database.start();
	    	
	    	frostserver = new GenericContainer<>("fraunhoferiosb/frost-server:1.9.3").withExposedPorts(8080)
					.withEnv("serviceRootUrl","http://localhost:8080/FROST-Server")
					.withEnv("http_cors_enable","true")
					.withEnv("http_cores_allowed.origins","*")
					.withEnv("persistence_db_driver","org.postgresql.Driver")
					.withEnv("persistence_db_url","jdbc:postgresql://database:5432/sensorthings")
					.withEnv("persistence_db_username","sensorthings")
					.withEnv("persistence_db_password","ChangeMe")
					.withEnv("persistence_autoUpdateDatabase","true")
					.withNetwork(network)
					.waitingFor(Wait.forHttp("/FROST-Server/v1.0/Datastreams").forStatusCode(200));
			frostserver.start();
			
			ready = true;	
		}
		Integer externalPort = frostserver.getMappedPort(8080);
		String url = "http://" + frostserver.getContainerIpAddress() + ":" + externalPort + "/FROST-Server/v1.0/";
    
    	System.out.println("FROST-Server-URL: "+ url);
    	
    	return url;
    	
	}
}
