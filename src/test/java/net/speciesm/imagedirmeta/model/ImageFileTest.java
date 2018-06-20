package net.speciesm.imagedirmeta.model;

import net.speciesm.imagedirmeta.model.exception.UnhandledFormatException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ImageFileTest {

    private ImageFile id;

    @BeforeMethod
    public void setUp() {
        try {
            id = new ImageFile(
                    Paths.get(getClass().getClassLoader().getResource("testfiles/subdir/test.jpg").getPath()));
        } catch (UnhandledFormatException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testGetType() {
        assertEquals(id.getType(), "JPEG-image");
    }

    @Test
    public void testGetCompressionFactor() {
        assertTrue(id.getCompressionFactor() > 0);
    }

    @Test
    public void testGetLength() {
        assertEquals(id.getFileSize(), 630);
    }

    @Test
    public void testGetImageFormat() {
        assertEquals(id.getImageFormat(), HandledFormat.JPEG);
    }

    @Test
    public void testGetWidth() {
        assertEquals(id.getWidth(), 100);
    }

    @Test
    public void testGetHeight() {
        assertEquals(id.getHeight(), 100);
    }
}
