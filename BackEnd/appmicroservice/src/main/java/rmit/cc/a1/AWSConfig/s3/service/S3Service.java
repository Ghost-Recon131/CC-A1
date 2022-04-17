package rmit.cc.a1.AWSConfig.s3.service;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.ItemListing.model.ItemImages;
import rmit.cc.a1.ItemListing.repository.ItemImagesRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class S3Service {

    @Autowired
    private S3Client amazonS3;
    @Autowired
    private ItemImagesRepository itemImagesRepository;

    private final Logger logger = LogManager.getLogger(this.getClass());

    public String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    // Save file to S3
    @Async
    public String saveImage(MultipartFile multipartFile, String fileName, Long imageID, Long listingID, Account currentUser){

        String s3BucketName = currentUser.getUserRole().toString().toLowerCase() + "-" + currentUser.getId() + "-" + currentUser.getUuid();
        String s3ObjectURL = null;
        String fileExtension = getFileExtension(fileName);
        InputStream fileInput = null;
        String fileKey = listingID + "-" + imageID + "." + fileExtension;  // This is the key for S3, made ie 1-1

        try {
            fileInput = multipartFile.getInputStream();
        }
        catch (Exception e) {
            logger.error("Error while deleting temp file \n" +  e.getLocalizedMessage());
        }

        // Creates request & uploads to S3, uploaded image can be viewed publicly
        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(s3BucketName).key(fileKey).acl(ObjectCannedACL.PUBLIC_READ).build();
            amazonS3.putObject(putObjectRequest, RequestBody.fromInputStream(fileInput, fileInput.available()));
        } catch (Exception e) {
            logger.error("Error uploading file \n" +  e.getLocalizedMessage());
        }

        // This section gets the file key and sets it as image name, and gets the file s3 url and saves to database
        try{
            s3ObjectURL = amazonS3.utilities().getUrl(builder -> builder.bucket(s3BucketName).key(fileKey)).toExternalForm();
            ItemImages toUpdate = itemImagesRepository.getById(imageID);
            toUpdate.setImageName(fileKey+fileExtension);
            toUpdate.setImageLink(s3ObjectURL);
            itemImagesRepository.save(toUpdate);
        } catch (Exception e){
            logger.error("Error getting S3 URL \n" +  e.getLocalizedMessage());
            System.err.println("Error getting S3 URL \n" +  e.getLocalizedMessage());
        }

        return s3ObjectURL;
    }

    // Delete image from S3
    public void deleteImage(String role, Long userID, String key){
        String bucketName = role + "-" + userID;
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(key).build();

        try{
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (Exception e){
            logger.info("Failed to delete object \n" + e);
            System.err.println("Bucket already exists! \n" + e);
        }

    }

    // Create new S3 Bucket if user does not already have one
    public void createS3Bucket(String role, Long userID, String uuid){
        String bucketName = role.toLowerCase() + "-" + userID + "-" + uuid;

        if(!doesBucketExist(bucketName)){
            try{
                CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket(bucketName).build();
                amazonS3.createBucket(createBucketRequest);
            } catch (Exception e){
                logger.error("Failed to create bucket " + e);
                System.err.println("Failed to create bucket \n" + e);
                System.err.println("userID received: " + userID);
                System.err.println("role received: " + role);
                System.err.println("Attempted Bucketname: " + bucketName);
            }
        }else{
            logger.info("Bucket already exists for user");
            System.err.println("Bucket already exists!");
        }

    }

    // Checks whether bucket exists
    private boolean doesBucketExist(String bucketName){
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
        try {
            amazonS3.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

    // Delete S3 Bucket
    public void deleteS3Bucket(String role, Long userID, String uuid){
        String bucketName = role.toLowerCase() + "-" + userID + "-" + uuid;

        if(doesBucketExist(bucketName)){
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
            try{
                amazonS3.deleteBucket(deleteBucketRequest);
            } catch (Exception e){
                logger.error("Failed to create bucket " + e);
                System.err.println("Failed to create bucket \n" + e);
            }
        }else{
            logger.info("Bucket does not exist");
            System.err.println("does not exist");
        }

    }

    // Debug method, gets list of all S3 Buckets
    public List<String> getS3BucketNames(){
        List<String> Names = new ArrayList<>();

        // Create request & get response
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = amazonS3.listBuckets(listBucketsRequest);

        List<Bucket> buckets = listBucketsResponse.buckets();

        for(Bucket bucket : buckets) {
            Names.add(bucket.name());
        }
        return Names;
    }

}
