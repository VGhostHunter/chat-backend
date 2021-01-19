package com.dhy.chat.web.controller;

import com.dhy.chat.dto.FileDto;
import com.dhy.chat.dto.Result;
import com.dhy.chat.web.config.properties.AppProperties;
import com.dhy.chat.web.config.properties.MinioProperties;
import io.minio.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author vghosthunter
 */
@Api("对象存储管理")
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final MinioClient minioClient;
    private final AppProperties appProperties;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ThreadLocal<SimpleDateFormat> sdfThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddhhmm"));

    public FileController(MinioClient minioClient,
                          AppProperties appProperties) {
        this.minioClient = minioClient;
        this.appProperties = appProperties;
    }

    @ApiOperation("头像上传")
    @PostMapping(value = "/profilePicture")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            var bucket = BucketExistsArgs.builder().bucket(MinioProperties.profilePicture).build();
            boolean isExist = minioClient.bucketExists(bucket);
            if (! isExist) {
                //创建存储桶并设置只读权限
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(MinioProperties.profilePicture).region("aws-cn").build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(MinioProperties.profilePicture).build());
            }
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            // 设置存储对象名称
            String objectName = sdfThreadLocal.get().format(new Date()) + "-" + filename;
//            Map<String, String> metadata = new HashMap<>();
//            metadata.put("My-Project", "Project One");
            // 使用putObject上传一个文件到存储桶中
            minioClient.putObject(PutObjectArgs.builder().contentType(file.getContentType())
                    .object(objectName)
//                    .userMetadata(metadata)
                    .bucket(MinioProperties.profilePicture).stream(file.getInputStream(), file.getSize() , -1).build());
            logger.info("文件上传成功!");

            FileDto fileDto = new FileDto();

            fileDto.setId(objectName);
            fileDto.setUrl(appProperties.getMinio().getEndPoint(), MinioProperties.profilePicture, objectName);
            return ResponseEntity.ok(fileDto);
        } catch (Exception e) {
            logger.info("上传发生错误: {}！", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.failure("上传失败"));
        }
    }
}
