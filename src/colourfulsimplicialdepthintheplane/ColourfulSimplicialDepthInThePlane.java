/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colourfulsimplicialdepthintheplane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Olga
 */
public class ColourfulSimplicialDepthInThePlane {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //long startTime = System.currentTimeMillis();
        Setup setup = new Setup();
        setup.firstPart();
        long CSD = setup.colourfulSimplicialDepth();
        //long endTime = System.currentTimeMillis();
        //double timeInSeconds = (endTime - startTime) / 1000.0;
        
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File(
                    new File("").getAbsolutePath() + "/data/plot.txt"),
                    true));
            writer.println(String.valueOf(setup.k).concat(" ")
                    .concat(String.valueOf(setup.n)).concat(" ")
                    .concat(String.valueOf(Flops.flops))
                    .concat(" ").concat(String.valueOf(CSD)));
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ColourfulSimplicialDepthInThePlane.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
