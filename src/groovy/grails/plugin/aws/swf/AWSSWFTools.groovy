package grails.plugin.aws.swf

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.simpleworkflow.model.StartWorkflowExecutionRequest
import com.amazonaws.services.simpleworkflow.model.TaskList
import com.amazonaws.services.simpleworkflow.model.WorkflowType

class AWSSWFTools {
	
	static String DEFAULT_CHILD_POLICY = 'TERMINATE'

	/**
	 * AWSCredentialsHolder injected by the plugin
	 */
	def credentialsHolder

	/**
	 * Start SWF workflow execution
	 * 
	 * @param domainName String that is the domain in which the workflow execution is created. This field is required.
	 * @param workflowId String that is the user-defined identifier associated with the workflow execution. This field is required.
	 * @param workflowName String that is the name of the workflow type. This field is required.
	 * @param workflowVersion String that is the version of the workflow type. This field is required.
	 * @param taskName String
	 * @param input String that is the input for the workflow execution
	 * @param tags List<String> of tags to associate with the workflow execution
	 * @return Run object that contains a runId
	 */
	def startWorkflowExecution(String domainName, 
							   String workflowId,
							   String workflowName, 
							   String workflowVersion,
							   String taskName,
							   String childPolicy=DEFAULT_CHILD_POLICY,
							   String input, 
							   Collection tags) {
		
			def credentials	 = credentialsHolder.buildAwsSdkCredentials()
			AmazonSimpleWorkflowClient swfService = new AmazonSimpleWorkflowClient(credentials)

			WorkflowType workflowType = new WorkflowType().withName(workflowName).withVersion(workflowVersion)

			TaskList taskList = new TaskList().withName(taskName)

			StartWorkflowExecutionRequest workflowRequest = new StartWorkflowExecutionRequest()
				.withDomain(domainName)
				.withWorkflowId(workflowId)
				.withWorkflowType(workflowType)
				.withTaskList(taskList)
				.withChildPolicy(childPolicy)
				.withInput(input)
				.withTagList(tags)

			return swfService.startWorkflowExecution(workflowRequest)
	}
}
