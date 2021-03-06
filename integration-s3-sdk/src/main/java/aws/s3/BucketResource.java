package aws.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Path("/s3")
public class BucketResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Long postFileToBucket(@Valid Arquivo arquivo) throws IOException {
		try {
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(BucketConfig.clientRegion)
					.withCredentials(new AWSStaticCredentialsProvider(BucketConfig.awsCreds)).build();

			InputStream input = new ByteArrayInputStream(arquivo.getFile());

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(arquivo.getContentType());

			PutObjectRequest request = new PutObjectRequest(BucketConfig.bucketName, arquivo.getId().toString(), input,
					metadata);

			request.setInputStream(input);
			request.setMetadata(metadata);

			s3Client.putObject(request);

		} catch (SdkClientException e) {
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
	@Produces(MediaType.APPLICATION_JSON)
	public Arquivo getFileFromBucket(@PathParam("id") String id) {
		try {
			final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
									.withRegion(BucketConfig.clientRegion)
									.withCredentials(new AWSStaticCredentialsProvider(BucketConfig.awsCreds))
									.build();

			S3Object o = s3.getObject(BucketConfig.bucketName, id);
			ObjectMetadata metadata = o.getObjectMetadata();

			S3ObjectInputStream s3in = o.getObjectContent();
			byte[] file = IOUtils.toByteArray(o.getObjectContent());

			s3in.close();

			return new Arquivo(id, metadata.getContentType(), file);
		} catch (AmazonServiceException e) {
			throw new WebApplicationException(e.getErrorMessage(), 500);
		} catch (FileNotFoundException e) {
			throw new WebApplicationException(e.getMessage(), 404);
		} catch (IOException e) {
			throw new WebApplicationException(e.getMessage(), 500);
		}
	}

}
