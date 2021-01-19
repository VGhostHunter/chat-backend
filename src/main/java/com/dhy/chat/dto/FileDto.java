package com.dhy.chat.dto;

/**
 * @author vghosthunter
 */
public class FileDto {

    private String id;

    private String name;

    private String url;

    private String extension;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        resolveNameFromId();
        resolveExtensionFromId();
    }

    private void resolveNameFromId() {
        name = id.substring(id.lastIndexOf("-") + 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String entryPoint, String bucket, String objectName) {
        this.url = entryPoint + "/" + bucket + "/" + objectName;
    }

    public String getExtension() {
        return extension;
    }

    private void resolveExtensionFromId() {
        extension = id.substring(id.lastIndexOf("."));
    }
}
