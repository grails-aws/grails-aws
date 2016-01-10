package grails.plugin.aws.swf

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.simpleworkflow.model.Run
import com.amazonaws.services.simpleworkflow.model.StartWorkflowExecutionRequest
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionAlreadyStartedException
import grails.plugin.aws.AWSCredentialsHolder
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
import org.junit.Test

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class AWSSWFToolsTests {

	AWSSWFTools swf

	void setUp() {
		swf = new AWSSWFTools()
		mockCredentialsHolder()
	}

	@Test
	void startReturnsRunIdAsString() {
		def expectedRunId = 'MockRunId5'

		def mock = new MockFor(AmazonSimpleWorkflowClient)
		mock.demand.startWorkflowExecution { return new Run(runId: expectedRunId) }
		mock.use {
			StartWorkflowExecutionRequest startRequest = new StartWorkflowExecutionRequest()
			assert swf.start(startRequest) == expectedRunId
		}
	}

	@Test
	void startReturnsNullWhenWorkflowAlreadyStarted() {

		def mock = new MockFor(AmazonSimpleWorkflowClient)
		mock.demand.startWorkflowExecution { throw new WorkflowExecutionAlreadyStartedException() }
		mock.use {
			StartWorkflowExecutionRequest startRequest = new StartWorkflowExecutionRequest()
			assert swf.start(startRequest) == null
		}
	}

	def mockCredentialsHolder() {
		swf.credentialsHolder = [
				buildAwsSdkCredentials: { ->
					new BasicAWSCredentials('mock access key', 'mock secret key')
				}
		] as AWSCredentialsHolder
	}

}
