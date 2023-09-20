package io.github.MillenniarSt.MillenniarTexture.ManagmentFile;

import javafx.concurrent.Task;
import javafx.event.Event;

import java.io.File;

/*             //////////////////////////////////
 *           //								     //
 *           //    //          //                //
 *           //    ////      ////    //////      //
 *           //	  //  //  //  //  //             //
 *           //	  //    //    //  //             //
 *           //	  //          //    //////       //
 *           //    //          //          //	 //
 *           //    //          //          //    //
 *           //    //          //  ////////      //
 *           //                                  //
 *             /////// Millenniar Studios ///////
 */

class ManagmentFile extends Task<Integer[]> {

    protected static final Integer[] voidProgress = {0, 0};

    private boolean stop = false;

    protected ManagmentFile() {

    }

    @Override
    protected Integer[] call() throws Exception {
        return null;
    }

    protected int countFile(File directory) {
        File[] files = directory.listFiles();
        int count = files.length;
        for(File file : files) {
            if(file.isDirectory())
                count = count + countFile(file);
        }
        return count;
    }

    public void execute(boolean dialog) {

    }

    protected void stop() {
        this.stop = true;
    }
    public boolean isStop() {
        return stop;
    }
}
