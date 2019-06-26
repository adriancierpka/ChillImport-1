package com.chillimport.server.utility;

import com.chillimport.server.FileManager;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;


@Service
public class SensorThingsServiceFactory {

    public SensorThingsService build() throws MalformedURLException, URISyntaxException {

        return new SensorThingsService(FileManager.getServerURL());

    }
}
