package grails.plugin.aws.swf

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.simpleworkflow.model.StartWorkflowExecutionRequest
import com.amazonaws.services.simpleworkflow.model.TaskList
import com.amazonaws.services.simpleworkflow.model.WorkflowType

class AWSSWFTools {
	
	static String DEFAULT_CHILD_POLICY = 'TERMINATE'

	// injected
	def credentialsHolder

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
