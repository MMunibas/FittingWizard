/*
 * Copyright (c) 2015, Florent Hedin, Markus Meuwly, and the University of Basel
 * All rights reserved.
 *
 * The 3-clause BSD license is applied to this software.
 * see LICENSE.txt
 *
 */
package ch.unibas.charmmtools.files.coordinates;

import java.io.File;
import java.io.IOException;

/**
 * This common interface  declares methods that several coordinates
 * files writers should implement
 * 
 * @author hedin
 */
public interface coordinates_writer {
    
    /**
     * Returns content of the writer object as a string
     * @return string corresponding to content of a file
     */
    public abstract String getTextContent();

    /**
     * Saves the file in a given directory
     * 
     * @param dir a directory where to save the file
     * @return returns the path of the written file for a possible direct use
     * @throws IOException Thrown if problem happens when writing file
     */
    public String writeFile(File dir) throws IOException;

    /**
     * Define content of the writer object using a string
     * @param content an input string containing some charmm content
     * @throws IOException Thrown if problem happens when writing file
     */
    public void setModifiedTextContent(String content) throws IOException;
    
    /**
     * Return the type of file being written : "pdb","psf", etc...
     * 
     * @return a string identifying the type of the current coordinates_writer
     */
    public String myID();
}
