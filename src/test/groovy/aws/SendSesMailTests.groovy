package aws

import grails.plugin.aws.AWSCredentialsHolder
import grails.plugin.aws.ses.SendSesMail
import grails.test.mixin.TestMixin
import org.grails.buffer.StreamCharBuffer
import org.junit.Test
import grails.test.mixin.support.GrailsUnitTestMixin

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull


@TestMixin(GrailsUnitTestMixin)
class SendSesMailTests {

   static final long DEFAULT_MAIL_ID = 63718397162749

    void setUp() {
      SendSesMail.metaClass.sendSimpleMail = { from, destination, message ->
         println "sending mail..."
         return DEFAULT_MAIL_ID
      }
    }
    
    @Test
    void buildSimpleSenderWithoutCatchAll() {

      def credentialsHolder       = new AWSCredentialsHolder()
      credentialsHolder.accessKey = "my-plain-access-key"
      credentialsHolder.secretKey = "my-plain-secret-key"

      def sender               = new SendSesMail()
      sender.from              = "default-from-email@server.com"
      sender.credentialsHolder = credentialsHolder

      assertEquals "default-from-email@server.com", sender.from
    }

    @Test
    void buildSimpleSenderWithCatchAll() {

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

    @Test
   void setFrom() {

      def credentialsHolder       = new AWSCredentialsHolder()
      credentialsHolder.accessKey = "my-plain-access-key"
      credentialsHolder.secretKey = "my-plain-secret-key"

      def sender               = new SendSesMail()
      sender.from              = "default-from-email@server.com"
      sender.credentialsHolder = credentialsHolder

      sender.from("new-from@server.com")
      assertEquals "new-from@server.com", sender.from
   }

    @Test
   void setTo() {

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

    @Test
   void setMultipleTo() {

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

    @Test
   void setCc() {

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

    @Test
   void setMultipleCc() {

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

    @Test
   void setBcc() {

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

    @Test
   void setMultipleBcc() {

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

    @Test
   void setBody() {

      def credentialsHolder       = new AWSCredentialsHolder()
      credentialsHolder.accessKey = "my-plain-access-key"
      credentialsHolder.secretKey = "my-plain-secret-key"

      def sender               = new SendSesMail()
      sender.from              = "default-from-email@server.com"
      sender.credentialsHolder = credentialsHolder

      sender.body("This is the mail body!")
      assertEquals "This is the mail body!", sender.body
   }

    @Test
   void setHtml() {

      def credentialsHolder       = new AWSCredentialsHolder()
      credentialsHolder.accessKey = "my-plain-access-key"
      credentialsHolder.secretKey = "my-plain-secret-key"

      def sender               = new SendSesMail()
      sender.from              = "default-from-email@server.com"
      sender.credentialsHolder = credentialsHolder

      sender.html("This is the html mail body!")
      assertEquals "This is the html mail body!", sender.html
   }

    @Test
   void setHtmlFromStreamCharBuffer() {

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

    @Test
   void setSubject() {

      def credentialsHolder       = new AWSCredentialsHolder()
      credentialsHolder.accessKey = "my-plain-access-key"
      credentialsHolder.secretKey = "my-plain-secret-key"

      def sender               = new SendSesMail()
      sender.from              = "default-from-email@server.com"
      sender.credentialsHolder = credentialsHolder

      sender.subject("E-mail subject")
      assertEquals "E-mail subject", sender.subject
   }

    @Test
   void setClosureData() {

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

    @Test
   void checkValidFromAddress_FailWithoutFrom() {

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

    @Test
   void checkValidFromAddress_OkWithFromInClosure() {

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

    @Test
   void checkValidFromAddress_OkWithFromInSetter() {

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

    @Test
   void buildTextMailMessage() {

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

    @Test
   void buildHtmlMailMessage() {

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

    @Test
   void buildTextAndHtmlMailMessage() {

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
   
   @Test
   void buildSimpleMessageDefaultsCharsetToUTF8() {
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

      def expectedCharset = 'UTF-8'
      assert message.subject.charset == expectedCharset
      assert message.body.html.charset == expectedCharset
      assert message.body.text.charset == expectedCharset
   }

    @Test
   void buildSimpleMessageAllowsCharsetOverride() {
      def credentialsHolder       = new AWSCredentialsHolder()
      credentialsHolder.accessKey = "my-plain-access-key"
      credentialsHolder.secretKey = "my-plain-secret-key"

      def sender               = new SendSesMail()
      sender.from              = "default-from-email@server.com"
      sender.credentialsHolder = credentialsHolder

      sender.send {
         charset "ISO-8859-1"
         from    "new-from@server.com"
          to      "new-to@server.com"
          subject "new subject"
          body    "e-mail body (plain text)"
          html    "e-mail body (html content)"
      }

      def message = sender.buildSimpleMessage()

      def expectedCharset = 'ISO-8859-1'
      assert message.subject.charset == expectedCharset
      assert message.body.html.charset == expectedCharset
      assert message.body.text.charset == expectedCharset
   }

    @Test
   void buildDestinationWithoutCatchAll() {

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

    @Test
   void buildDestinationWithCatchAll() {

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
    
    @Test
    void setRegionGoodVal(){
        def sender = new SendSesMail()
        sender.region = 'US_EAST_1'

        assertEquals(sender.awsRegion.getName(), 'us-east-1')
    }

    @Test
    void setRegionBadVal(){
        def sender = new SendSesMail()
        shouldFail{
            sender.region = 'US_EAST_1_badvalue'
        }
    }
    
    @Test
    void setRegionNull(){
        def sender = new SendSesMail()
        sender.region = null

        assertEquals(sender.awsRegion.getName(), 'us-east-1')
    }
}
