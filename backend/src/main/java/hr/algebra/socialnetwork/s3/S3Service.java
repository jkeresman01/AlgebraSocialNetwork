package hr.algebra.socialnetwork.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  public void putObject(String key, byte[] data) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(data.length);
    amazonS3.putObject(bucketName, key, new ByteArrayInputStream(data), metadata);
  }

  public byte[] getObject(String key) {
    try (S3ObjectInputStream s3is = amazonS3.getObject(bucketName, key).getObjectContent()) {
      return s3is.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException("Failed to read file from S3", e);
    }
  }
}
