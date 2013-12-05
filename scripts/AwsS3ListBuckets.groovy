import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client

includeTargets << new File(awsPluginDir, "scripts/_ReadAwsCredentials.groovy")

target(awsS3ListBuckets: "list S3 Buckets") {


	depends (readAwsCredentials)

	def credentials = new BasicAWSCredentials(accessKey, secretKey)
	def s3 = new AmazonS3Client(credentials)

	def buckets = s3.listBuckets()
	
	if (buckets) {

		println "[AWS S3]  -------------------------------------------------------------------------------"
		println "[AWS S3] |    creation date | name                                                       |"
		println "[AWS S3] |-------------------------------------------------------------------------------|"

		buckets.each { bucket ->

			def _creationDate = bucket.creationDate.format('yyyy/MM/dd HH:mm')
			def _name = bucket.name.padRight(58)

			println "[AWS S3] | ${_creationDate} | ${_name} |"
		}

		println "[AWS S3]  -------------------------------------------------------------------------------"

	} else {
		println "[AWS S3] No buckets available for this account"
	}
}
setDefaultTarget(awsS3ListBuckets)
