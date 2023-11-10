package me.quartz.hungergames.files;

public class FileManager {

    private final CustomFile mapsFile;
    private final CustomFile tiersFile;

    public FileManager() {
        this.mapsFile = new CustomFile("maps");
        this.tiersFile = new CustomFile("tiers");
    }

    public CustomFile getMapsFile() {
        return mapsFile;
    }

    public CustomFile getTiersFile() {
        return tiersFile;
    }
}
