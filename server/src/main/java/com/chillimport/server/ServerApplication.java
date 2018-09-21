package com.chillimport.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ServerApplication {

    /**
     * @param args start up arguments
     */
    public static void main(String[] args) {

        String value = System.getenv("serverurl");
        if (value == null) {
            System.out.println("Server url was not specified. Peacefully aborting.");
            return;
        }
        FileManager.setServerURLOnStartup(value);
        String confvalue = System.getenv("basepath");
        if (confvalue == null) {
            System.out.println("Config path was not specified. Peacefully aborting.");
            return;
        }
        FileManager.setPathsOnStartup(confvalue);
        String username = FileManager.readFromFile("username.cfg");

        if (!username.equals("")) {
            FileManager.setUsernameOnStartup(username);
            System.out.println("Found user name");
        }
        SpringApplication.run(ServerApplication.class, args);

    }
}
