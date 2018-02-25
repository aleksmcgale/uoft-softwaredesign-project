package image.filenamestrategy;


import image.ImageFile;

/**
 * A FileTags name strategy. Returns string representations of tags in an ImageFile's file name.
 */
public class FileTags implements FileNameStrategy {
    /**
     * Whether or not to return the string representations of tags with the '@' label.
     */
    private boolean withTagLabel;

    /**
     * Constructs a new FileTags name strategy. Specifies whether this strategy should return the tag labels in an
     * ImageFile's name.
     *
     * @param withTagLabel Whether or not to return the tag labels in an ImageFile's name with their '@' label.
     */
    public FileTags(boolean withTagLabel) {
        this.withTagLabel = withTagLabel;
    }

    /**
     * Returns the tags (with or without the '@' label) in an ImageFile's file name.
     *
     * @param imageFile ImageFile for which to get tags.
     * @return A string with all of the tags in an ImageFile's name, separated by spaces.
     */
    public String getName(ImageFile imageFile) {
        String[] splitName = new FileName(false).getName(imageFile).split("\\s+");
        StringBuilder tags = new StringBuilder("");

        for (String s : splitName) {
            // if it is a tag
            if (s.contains("@")) {
                if (!withTagLabel) {
                    s = s.replace("@", "");
                }
                tags.append(s);
                tags.append(" ");
            }

        }

        return tags.toString();
    }

}
