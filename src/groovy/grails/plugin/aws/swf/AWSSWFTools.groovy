package grails.plugin.aws.swf

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.simpleworkflow.model.DescribeWorkflowExecutionRequest
import com.amazonaws.services.simpleworkflow.model.Run
import com.amazonaws.services.simpleworkflow.model.StartWorkflowExecutionRequest
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionAlreadyStartedException
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionDetail

class AWSSWFTools {

    private static Logger log = LoggerFactory.getLogger(this)
    
    /**
     * AWSCredentialsHolder injected by the plugin
     */
    def credentialsHolder

    /**
     * Start SWF workflow execution
     * @param StartWorkflowRequest
     * @return String that is the started workflow's run id. NULL if workflow already started
     */
    String start(StartWorkflowExecutionRequest startWorkflowRequest) {

        def credentials  = credentialsHolder.buildAwsSdkCredentials()

        AmazonSimpleWorkflowClient swfClient = new AmazonSimpleWorkflowClient(credentials)

        Run result
        try {
            result = swfClient.startWorkflowExecution(startWorkflowRequest)
            log.debug("WorkflowExecution started. runId: $result.runId")
        } catch (WorkflowExecutionAlreadyStartedException e) {
            log.warn("WorkflowExecution already started. $e.message")
        }
        return result?.runId
    }
    
    /**
     * Returns information about the specified workflow execution including its type and some statistics.
     * @param describeWorkflowRequest
     * @return WorkflowExecutionDetail
     */
    WorkflowExecutionDetail describe(DescribeWorkflowExecutionRequest describeWorkflowRequest) {

        def credentials  = credentialsHolder.buildAwsSdkCredentials()

        AmazonSimpleWorkflowClient swfClient = new AmazonSimpleWorkflowClient(credentials)

        return swfClient.describeWorkflowExecution(describeWorkflowRequest)
    }
}
