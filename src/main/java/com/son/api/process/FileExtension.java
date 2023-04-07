package com.son.api.process;

import java.util.Optional;

public enum FileExtension {

    CSV("csv")
    ,TXT("txt");

    private String name;

    FileExtension(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<FileExtension> getFileExtension(String name) {
        for (FileExtension fileExtension : FileExtension.values()){
            if (fileExtension.getName().equalsIgnoreCase(name)){
                return Optional.of(fileExtension);
            }
        }
        return Optional.empty();
    }

    public static boolean contains(String name) {
        for (FileExtension fileExtension : FileExtension.values()){
            if (fileExtension.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

}
