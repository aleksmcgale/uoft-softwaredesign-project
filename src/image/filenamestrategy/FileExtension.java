package image.filenamestrategy;

import image.ImageFile;

/**
 * A FileExtension. Returns the file extension / file type of an ImageFile. Implements FileNameStrategy.
 */
public class FileExtension implements FileNameStrategy {

    /**
     * Returns the file extension of a specified ImageFile.
     *
     * @param imageFile ImageFile for which to get the file extension.
     * @return The file extension of the specifed ImageFile.
     */
    @Override
    public String getName(ImageFile imageFile) {
        String fullName = imageFile.getImageFilePath().getName();
        return fullName.substring(fullName.lastIndexOf("."), fullName.length());
    }
}
