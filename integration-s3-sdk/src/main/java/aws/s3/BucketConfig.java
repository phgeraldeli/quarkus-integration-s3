package aws.s3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

public final class BucketConfig {
	private BucketConfig() {
	}

	public static final String bucketName = System.getenv("BUCKET_NAME");
	public static final BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv("AWS_KEY_ACCESS"),
			System.getenv("AWS_SECRET_KEY"));
	public static final Regions clientRegion = Regions.US_EAST_2;
}
