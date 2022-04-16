package rmit.cc.a1.AWSConfig.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    // View all buckets at: https://s3.console.aws.amazon.com/s3/buckets?region=us-east-1&region=us-east-1
    @Value("${access.key.id}")
    private String accessKeyID;

    @Value("${access.key.secret}")
    private String accessKeySecret;

    @Value("${s3.region.name}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return  S3Client.builder().region(Region.of(region)).credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(accessKeyID, accessKeySecret))).build();
    }

}
