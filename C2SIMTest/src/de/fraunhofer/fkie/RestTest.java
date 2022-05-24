package de.fraunhofer.fkie;

import edu.gmu.c4i.c2simclientlib2.C2SIMClientException;
import edu.gmu.c4i.c2simclientlib2.C2SIMClientREST_Lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RestTest {

    static C2SIMClientREST_Lib restLib = new C2SIMClientREST_Lib(
            "C2LG",
            "ALL",
            "Inform",
            "1.0.1");

    public static void main(String[] args) {
        String xml = "";
        try {
            xml = new String(
                    Files.readAllBytes(
                            Paths.get("C:\\Projekte\\test\\git\\C2SIMTest\\testdata\\CWIX-2022 PHASE 2 OPORD Task 1BnCp3-1.xml")));
            restLib.c2simRequest(xml);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (C2SIMClientException e) {
            e.printStackTrace();
        }
    }
}
