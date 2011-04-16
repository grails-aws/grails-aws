import org.jets3t.service.S3Service
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

def credentialsHolder = AH.application?.mainContext?.credentialsHolder
println S3Service.buildPostForm("public-bucket", "file-name.jpg")
println "------------------------------------------"

def bucketName = "test-bucket"
def key = "uploads/images/pic.jpg"
def inputFields = [
    "<input type=\"hidden\" name=\"acl\" value=\"public-read\">",
    "<input type=\"hidden\" name=\"Content-Type\" value=\"image/jpeg\">",
    "<input type=\"hidden\" name=\"success_action_redirect\" value=\"http://localhost/post_upload\">"
] as String[]
def conditions = [
    S3Service.generatePostPolicyCondition_Equality("bucket", bucketName),
    S3Service.generatePostPolicyCondition_Equality("key", key),
    S3Service.generatePostPolicyCondition_Range(10240, 204800),
    S3Service.generatePostPolicyCondition_Equality("acl", "public-read"),
    S3Service.generatePostPolicyCondition_Equality("Content-Type", "image/jpeg"),
    S3Service.generatePostPolicyCondition_Equality("success_action_redirect", "http://localhost/post_upload")
] as String[]

// Form will expire in 24 hours
def cal = Calendar.getInstance()
cal.add(Calendar.HOUR, 24)
def expiration = cal.getTime()

// Generate the form.
def restrictedForm = S3Service.buildPostForm(
    bucketName, key, credentialsHolder.buildJetS3tCredentials(), expiration, conditions, 
    inputFields, null, true, false, "Enviar aquivo")

println restrictedForm