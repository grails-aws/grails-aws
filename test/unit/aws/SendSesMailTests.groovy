package aws

import grails.plugin.aws.AWSCredentialsHolder
import grails.plugin.aws.ses.SendSesMail
import grails.test.GrailsUnitTestCase

import org.codehaus.groovy.grails.web.util.StreamCharBuffer

class SendSesMailTests extends GrailsUnitTestCase {

	static final long DEFAULT_MAIL_ID = 63718397162749

    protected void setUp() {
        super.setUp()
		SendSesMail.metaClass.sendSimpleMail = { from, destination, message ->
			println "sending mail..."
			return DEFAULT_MAIL_ID
		}
    }

    void testBuildSimpleSenderWithoutCatchAll() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		assertEquals "default-from-email@server.com", sender.from
    }

    void testBuildSimpleSenderWithCatchAll() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.catchall          = "catch-all@server.com"
		sender.credentialsHolder = credentialsHolder

		assertEquals "default-from-email@server.com", sender.from
		assertEquals "catch-all@server.com", sender.catchall
    }

	void testSetFrom() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.from("new-from@server.com")
		assertEquals "new-from@server.com", sender.from
	}

	void testSetTo() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.to("new-to@server.com")
		assertEquals 1, sender.to.size()
		assertEquals "new-to@server.com", sender.to[0]
	}

	void testSetMultipleTo() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.to("new-to-1@server.com", "new-to-2@server.com")
		assertEquals 2, sender.to.size()
		assertEquals "new-to-1@server.com", sender.to[0]
		assertEquals "new-to-2@server.com", sender.to[1]
	}

	void testSetCc() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.cc("new-cc@server.com")
		assertEquals 1, sender.cc.size()
		assertEquals "new-cc@server.com", sender.cc[0]
	}

	void testSetMultipleCc() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.cc("new-cc-1@server.com", "new-cc-2@server.com")
		assertEquals 2, sender.cc.size()
		assertEquals "new-cc-1@server.com", sender.cc[0]
		assertEquals "new-cc-2@server.com", sender.cc[1]
	}

	void testSetBcc() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.bcc("new-bcc@server.com")
		assertEquals 1, sender.bcc.size()
		assertEquals "new-bcc@server.com", sender.bcc[0]
	}

	void testSetMultipleBcc() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.bcc("new-bcc-1@server.com", "new-bcc-2@server.com")
		assertEquals 2, sender.bcc.size()
		assertEquals "new-bcc-1@server.com", sender.bcc[0]
		assertEquals "new-bcc-2@server.com", sender.bcc[1]
	}

	void testSetBody() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.body("This is the mail body!")
		assertEquals "This is the mail body!", sender.body
	}

	void testSetHtml() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.html("This is the html mail body!")
		assertEquals "This is the html mail body!", sender.html
	}

	void testSetHtmlFromStreamCharBuffer() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def buffer = new StreamCharBuffer()
		buffer.writer << "Testing mail body from StreamCharBuffer"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.html(buffer)
		assertEquals "Testing mail body from StreamCharBuffer", sender.html
	}

	void testSetSubject() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.subject("E-mail subject")
		assertEquals "E-mail subject", sender.subject
	}

	void test_SetClosureData() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.setClosureData {
			from    "new-from@server.com"
		    to      "new-to@server.com"
		    subject "new subject"
		    body    "e-mail body (plain text)"
			html    "e-mail body (html content)"
		}

		assertEquals "new-from@server.com", sender.from
	    assertEquals "new-to@server.com", sender.to[0]
	    assertEquals "new subject", sender.subject
	    assertEquals "e-mail body (plain text)", sender.body
		assertEquals "e-mail body (html content)", sender.html

	}

	void test_CheckValidFromAddress_FailWithoutFrom() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.credentialsHolder = credentialsHolder

		shouldFail {
			sender.send {
		    	to      "new-to@server.com"
		    	subject "new subject"
		    	body    "e-mail body (plain text)"
				html    "e-mail body (html content)"
			}
		}
	}

	void test_CheckValidFromAddress_OkWithFromInClosure() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.credentialsHolder = credentialsHolder

		sender.send {
			from    "new-from@server.com"
		    to      "new-to@server.com"
		    subject "new subject"
		    body    "e-mail body (plain text)"
			html    "e-mail body (html content)"
		}
	}

	void test_CheckValidFromAddress_OkWithFromInSetter() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.send {
		    to      "new-to@server.com"
		    subject "new subject"
		    body    "e-mail body (plain text)"
			html    "e-mail body (html content)"
		}
	}

	void test_BuildTextMailMessage() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.send {
			from    "new-from@server.com"
		    to      "new-to@server.com"
		    subject "new subject"
		    body    "e-mail body (plain text)"
		}

		def message = sender.buildSimpleMessage()
		assertNotNull message
		assertEquals  "new subject", message.subject.data
		assertEquals  "e-mail body (plain text)", message.body.text.data
	}

	void test_BuildHtmlMailMessage() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.send {
			from    "new-from@server.com"
		    to      "new-to@server.com"
		    subject "new subject"
		    html    "e-mail body (html content)"
		}

		def message = sender.buildSimpleMessage()
		assertNotNull message
		assertEquals  "new subject", message.subject.data
		assertEquals  "e-mail body (html content)", message.body.html.data
	}

	void test_BuildTextAndHtmlMailMessage() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.send {
			from    "new-from@server.com"
		    to      "new-to@server.com"
		    subject "new subject"
		    body    "e-mail body (plain text)"
		    html    "e-mail body (html content)"
		}

		def message = sender.buildSimpleMessage()
		assertNotNull message
		assertEquals  "new subject", message.subject.data
		assertEquals  "e-mail body (html content)", message.body.html.data
		assertEquals  "e-mail body (plain text)", message.body.text.data
	}

	void test_BuildDestinationWithoutCatchAll() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.send {
			from    "new-from@server.com"
		    to      "new-to@server.com"
			cc      "new-cc-1@server.com", "new-cc-2@server.com"
			bcc     "new-bcc-1@server.com", "new-bcc-2@server.com", "new-bcc-3@server.com"
		    subject "new subject"
		    body    "e-mail body (plain text)"
		    html    "e-mail body (html content)"
		}

		def destination = sender.buildSimpleDestination()
		assertNotNull destination
		assertEquals  1, destination.toAddresses.size()
		assertEquals  2, destination.ccAddresses.size()
		assertEquals  3, destination.bccAddresses.size()
		assertEquals  "new-to@server.com", destination.toAddresses[0]
		assertEquals  "new-cc-1@server.com", destination.ccAddresses[0]
		assertEquals  "new-cc-2@server.com", destination.ccAddresses[1]
		assertEquals  "new-bcc-1@server.com", destination.bccAddresses[0]
		assertEquals  "new-bcc-2@server.com", destination.bccAddresses[1]
		assertEquals  "new-bcc-3@server.com", destination.bccAddresses[2]
	}

	void test_BuildDestinationWithCatchAll() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def sender               = new SendSesMail()
		sender.from              = "default-from-email@server.com"
		sender.catchall          = "catchall@server.com"
		sender.credentialsHolder = credentialsHolder

		sender.send {
			from    "new-from@server.com"
		    to      "new-to@server.com"
			cc      "new-cc@server.com"
			bcc     "new-bcc@server.com"
		    subject "new subject"
		    body    "e-mail body (plain text)"
		    html    "e-mail body (html content)"
		}

		def destination = sender.buildSimpleDestination()
		assertNotNull destination
		assertEquals  1, destination.toAddresses.size()
		assertEquals  0, destination.ccAddresses.size()
		assertEquals  0, destination.bccAddresses.size()
		assertEquals  "catchall@server.com", destination.toAddresses[0]
	}
}
