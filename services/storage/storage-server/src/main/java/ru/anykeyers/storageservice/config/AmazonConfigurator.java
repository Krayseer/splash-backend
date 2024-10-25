package ru.anykeyers.storageservice.config;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
public class AmazonConfigurator {

    public AmazonS3 createAmazonS3Client(String accessKeyId, String secretAccessKey) {
        try {
            return AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(
                                    "https://storage.yandexcloud.net", "ru-central1"
                            )
                    )
                    .withCredentials(
                            new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey))
                    )
                    .build();
        } catch (SdkClientException e) {
            log.error("Error creating client for Object Storage via AWS SDK. Reason: {}", e.getMessage());
            throw new SdkClientException(e.getMessage());
        }
    }

}

