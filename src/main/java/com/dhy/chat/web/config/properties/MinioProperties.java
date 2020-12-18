package com.dhy.chat.web.config.properties;

/**
 * @author vghosthunter
 */
public class MinioProperties {

    private String accessKey;

    private String secretKey;

    private String endPoint;

    /**
     * 存储桶命名规则
     * 以下规则适用于为 S3 存储桶命名：
     * 存储桶名称必须介于 3 到 63 个字符之间。
     * 存储桶名称只能由小写字母、数字、句点 (.) 和连字符 (-) 组成。
     * 存储桶名称必须以字母或数字开头和结尾。
     * 存储桶名称不得采用 IP 地址格式（例如，192.168.5.4）。
     * 存储桶名称不能以 xn-- 开头（对于 2020 年 2 月之后创建的存储桶）。
     * 存储桶名称在分区中必须唯一。分区是一组区域。AWS 目前有三个分区：aws（标准区域）、aws-cn（中国区域）和 aws-us-gov（AWS GovCloud [美国] 区域）。
     * 用户头像
     */
    public static String profilePicture = "profile-picture";

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
