# ImageDirMeta

ImageDirMeta displays metadata about pictures and directories containing pictures. Reads all metadata on first scan, ignores non-image files in directories.

Currently parsing: GIF (single images, no animations), JPEG

## Building
This project uses Gradle version 4 or greater to build. If a local gradle is not installed, use the included wrapper `./gradlew` (for Linux) or `gradlew.bat` (for Windows)


Build all classes:
`gradle build`

Create JavaDoc:
`gradle javadoc`

Create a distributable jar packaged in a zip
`gradle distZip`

Output will be placed inside the `build` directory.

## Usage
`./bin/imagedirmeta <Path to a directory containing JPEG or GIF images>`
