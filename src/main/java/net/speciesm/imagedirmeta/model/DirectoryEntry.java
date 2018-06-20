package net.speciesm.imagedirmeta.model;

/**
 * Represents an entry in a directory
 *
 * @author Michael Gajda michael.gajda@tu-dortmund.de
 */
public interface DirectoryEntry {


    /**
     * Returns a description of the type
     * @return the type
     */
    String getType();

    /**
     * Returns the total size of an entry
     * @return size in bytes
     */
    long getFileSize();

    /**
     * Returns width-metadata about an entry
     * @return the width in pixel
     */
    int getWidth();

    /**
     * Returns height-metadata about an entry
     * @return the height in pixel
     */
    int getHeight();

    /**
     * Returns a value representing an approximated compression factor of an entry. If the file type
     * is unknown it returns 1.
     * @return the compression factor from 0 to 1
     */
    double getCompressionFactor();
}
