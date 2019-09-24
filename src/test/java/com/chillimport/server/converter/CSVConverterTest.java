package com.chillimport.server.converter;

import com.chillimport.server.FileManager;
import com.chillimport.server.Table;
import com.chillimport.server.config.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class CSVConverterTest {


    //http://hamcrest.org/JavaHamcrest/


    Configuration obj1;
    Table t;

    @Before
    public void setUp() throws Exception {
        String zidc1 = "ECT";
        StringColumn[] dateTime1 = {new StringColumn("TT-HH", 10), new StringColumn("MM-SS", 12)};
        int[] arr1 = {2, 8, 5};
        StreamObservation[] streamData1 = {new StreamObservation(123456, true, arr1)};
        MagicNumberMap[] map1 = new MagicNumberMap[3];

        map1[0] = new MagicNumberMap(1, "true", "wahr");
        map1[1] = new MagicNumberMap(2, "wahr", "true");
        map1[2] = new MagicNumberMap(4, "trueheit", "wahrheit");
        
        URL dummyURL = new URL("https://www.google.de/");

        obj1 = new Configuration(10, "aConfiguration", ";", 2, zidc1, dateTime1, streamData1, map1, DataType.CSV, dummyURL);
    }

    @Test
    public void convert() {
        File file = new File("src/test/java/com/chillimport/server/converter/files/testcsv.csv");

        try {
            t = CSVConverter.convert(file, obj1);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void convertBack() {
        convert();
        try {
            CSVConverter.convertBack(t, obj1, "src/test/java/com/chillimport/server/converter/files/exportcsv.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void filePreview() throws IOException {
        FileManager fm = new FileManager();
        ArrayList<ArrayList<String>> ll = CSVConverter.filePreview(new File(fm.getLogPath() + "/returnRows/2018-08-27T15-18-42--skippedRows.csv"),
                                                                     ConfigurationManager.loadConfig(-1365040327),
                                                                     4);
        String[] content = new String[8];
        content[0] = "Rosen sind rot";
        content[1] = "Veilchen sind Blau";
        content[2] = "Ich hasse Gedichte";
        content[3] = "Klopapier";
        content[4] = "Rosen sind rott";
        content[5] = "Veilchen sind Blauu";
        content[6] = "Ich hasse Gedichtee";
        content[7] = "Klopapierr";
        int curr = 0;
        for (ArrayList<String> s : ll) {
            for (String str : s) {
                assertEquals(content[curr], str);
                curr++;
            }
        }
    }

}
