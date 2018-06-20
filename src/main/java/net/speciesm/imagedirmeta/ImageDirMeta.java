package net.speciesm.imagedirmeta;

import lombok.extern.slf4j.Slf4j;
import net.speciesm.imagedirmeta.model.DirectoryEntry;
import net.speciesm.imagedirmeta.model.ImageDirectory;

import java.nio.file.Paths;

/**
 * ImageDirMeta
 *
 * @author Michael Gajda michael.gajda@tu-dortmund.de
 */
@Slf4j(topic = "ImageDirMeta")
public class ImageDirMeta {
    private static final int WHITESPACE = 12;

    public static void main(String[] args) {
        if (args.length == 0) {
            log.error("No source directory specified.");
            System.exit(1);
        }

        for (String arg : args) {
            ImageDirectory imageDir = new ImageDirectory(Paths.get(arg));
            printInfos(imageDir);
            // do something with imageDir
        }
    }

    /**
     * Recursively prints all metadata of an DirectoryEntry
     *
     * @param entry the root entry
     */
    private static void printInfos(DirectoryEntry entry) {
        String out = String.format("name: %s, type: %s%n", entry, entry.getType());
        out += String.format("  %-" + WHITESPACE + "s %d%n", "filesize:", entry.getFileSize());
        out += String.format("  %-" + WHITESPACE + "s %dx%d%n", "dimensions:", entry.getWidth(), entry.getHeight());
        out += String.format("  %-" + WHITESPACE + "s %d%%%n", "compression:", (int) (entry.getCompressionFactor() * 100.0));
        System.out.println(out);

        if (entry instanceof ImageDirectory) {
            ((ImageDirectory) entry).getChildEntries().forEach(ImageDirMeta::printInfos);
        }
    }
}
