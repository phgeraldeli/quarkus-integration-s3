package aws.s3;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Path("/s3")
public class BucketResource {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Long postFileToBucket(Arquivo arquivo) {
		Regions clientRegion = Regions.US_EAST_2;
		String bucketName = System.getenv("BucketName");
		
		try {
			// Não é necessário caso o sistema já tenha configurado como DEFAULT um usuário com credencial
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv("access_key_id"), System.getenv("secret_key_id"));
			
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();
			
			PutObjectRequest request = new PutObjectRequest(bucketName, String.format("FileId%d", arquivo.getId()), arquivo.getFile());
			ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            request.setMetadata(metadata);
            s3Client.putObject(request);
            
		} catch ( SdkClientException e ) {
			e.printStackTrace();
		}
		
		return arquivo.getId();
	}

	@POST
	@Path("/multipart")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Long postFileToBucketWithMultiPart(Arquivo arquivo) {
		throw new WebApplicationException(501);
	}
	
	@GET
	@Path("{id}")
	public File getFileFromBucket(@PathParam("id") String id) {
		throw new WebApplicationException(501);
	}
}
