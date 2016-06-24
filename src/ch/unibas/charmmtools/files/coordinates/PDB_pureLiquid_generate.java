/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unibas.charmmtools.files.coordinates;

import ch.unibas.fittingwizard.application.scripts.base.ScriptExecutionException;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author hedin
 */
public class PDB_pureLiquid_generate{

    protected static final Logger logger = Logger.getLogger(PDB_pureLiquid_generate.class);

    protected final String script;
    protected final File work_directory;
    protected final String pdb;
    protected final String top;
    protected final String par;

    public PDB_pureLiquid_generate(File _wdir, File _pdb) {
        script = new File("scripts/lj-fit/src/liquid.sims/generate.liq.sh").getAbsolutePath();
        work_directory = _wdir;
        pdb = _pdb.getAbsolutePath();
        top = new File("default/top_all36_cgenff.rtf").getAbsolutePath();
        par = new File("default/par_all36_cgenff.prm").getAbsolutePath();
    }

    public PDB_pureLiquid_generate(File _wdir, File _pdb, File _top, File _par) {
        script = new File("scripts/lj-fit/src/liquid.sims/generate.liq.sh").getAbsolutePath();
        work_directory = _wdir;
        pdb = _pdb.getAbsolutePath();
        top = _top.getAbsolutePath();
        par = _par.getAbsolutePath();
    }
        
    public void generate() {

        File myDir = this.work_directory;
//        File outfile = new File(myDir, "pure_liquid.pdb");

        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(myDir);
        pb.inheritIO();
        
        // missing arguments: mol.top mol.pdb mol.par Nmols L_size
        pb.command("/bin/bash", script, top, pdb, par, "256", "30");

        logger.info("Running bash script\n" + pb.command()
                + "\nin directory:\n" + pb.directory()
                + "\nwith environment:\n" + pb.environment());
        int exitCode = 0;
        try {
            Process p = pb.start();
            exitCode = p.waitFor();
            logger.info("Bash return value: " + exitCode);
            if (exitCode != 0) {
                throw new ScriptExecutionException(
                        String.format("Bash script [%s] did not exit correctly. Exit code: %s",
                                script,
                                String.valueOf(exitCode)));
            } else {

            }
        } catch (IOException | InterruptedException | ScriptExecutionException e) {
            throw new RuntimeException(String.format("Bash script [%s] failed.", script), e);
        }

    } // end generate()

} // END CLASS
