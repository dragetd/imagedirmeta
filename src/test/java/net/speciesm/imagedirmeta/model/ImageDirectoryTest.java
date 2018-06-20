package net.speciesm.imagedirmeta.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ImageDirectoryTest {

    private ImageDirectory id;

    @BeforeMethod
    public void setUp() {
        id = new ImageDirectory(
                Paths.get(getClass().getClassLoader().getResource("testfiles").getPath()));
    }

    @Test
    public void testGetChildEntries() {
        assertEquals(id.getChildEntries().size(), 3);
    }

    @Test
    public void testGetType() {
        assertEquals(id.getType(), "directory");
    }

    @Test
    public void testGetLength() {
        assertEquals(id.getFileSize(), 1555);
    }

    @Test
    public void testGetWidth() {
        assertEquals(id.getWidth(), 133);
    }

    @Test
    public void testGetHeight() {
        assertEquals(id.getHeight(), 133);
    }

    @Test
    public void testGetCompressionFactor() {
        assertTrue(id.getCompressionFactor() > 0);
    }
}
