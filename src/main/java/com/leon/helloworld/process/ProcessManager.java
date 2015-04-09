package com.leon.helloworld.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ImportResource("classpath:applicationContext.xml")
public class ProcessManager {

	// 获取配置文件后，引擎开始创建数据库。
	// private static ProcessEngine processEngine =
	// ProcessEngines.getDefaultProcessEngine();
	@Autowired
	private static ProcessEngine processEngine;
	// 获取流程储存服务组件
	@Autowired
	private static RepositoryService repositoryService;
	// 任务服务类
	@Autowired
	private static TaskService taskService;
	// 历史服务
	@Autowired
	private static HistoryService historyService;
	// 表单服务
	@Autowired
	private static FormService formService;
	// 管理服务
	@Autowired
	private static ManagementService managementService;
	// 运行时服务
	@Autowired
	private static RuntimeService runtimeService;
	//
	@Autowired
	private static IdentityService identityService;
	
	
	public ProcessManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isExistence(String processId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processId).singleResult();
		if (pi != null) {
			return true;
		} else {
			return false;
		}
	}

	// 获取所有的流程定义
	public List<ProcessDefinition> getAllProcessDefs() {

		return repositoryService.createProcessDefinitionQuery().list();
	}

	// 根据流程Id获取流程定义
	public ProcessDefinitionEntity getProcessDefByProcessId(String processId) {
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processId);
		System.out.println("得到流程定义ID:" + processId);
		return def;
	}

	// 获取所有的流程定义
	public List<ProcessDefinition> getAllProcessDefsByUserName(String userName) {

		List<ProcessDefinition> proDefs = new ArrayList<ProcessDefinition>();

		List<String> proIds = getAllProcessDefId();
		// 遍历
		for (String pId : proIds) {
			ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(pId);
			System.out.println("流程定义ID:" + pId);

			Set<Expression> users = def.getCandidateStarterUserIdExpressions();
			Set<Expression> groups = def
					.getCandidateStarterGroupIdExpressions();

			// 判定是否为合法用户
			if (isValidUser(userName, users)
					|| isValidGroupUser(userName, groups)) {

				proDefs.add(repositoryService.getProcessDefinition(pId));
			}
		}

		return proDefs;
	}

	public List<ProcessDefinition> getAllProcessDefsByGroupName(String groupName) {

		List<ProcessDefinition> proDefs = new ArrayList<ProcessDefinition>();

		List<String> proIds = getAllProcessDefId();
		// 遍历
		for (String pId : proIds) {
			ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(pId);
			System.out.println("流程定义ID:" + pId);

			Set<Expression> groups = def
					.getCandidateStarterGroupIdExpressions();

			// 判定是否为合法用户
			if (isValidGroupUser(groupName, groups)) {

				proDefs.add(repositoryService.getProcessDefinition(pId));
			}
		}

		return proDefs;
	}

	private boolean isValidGroupUser(String groupName, Set<Expression> groups) {
		// TODO Auto-generated method stub
		if (groups.contains(groupName)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidUser(String userName, Set<Expression> users) {
		// TODO Auto-generated method stub
		return true;
	}

	// 获取留有流程定义的ID
	public List<String> getAllProcessDefId() {

		List<String> proIds = new ArrayList<String>();

		List<ProcessDefinition> pList = getAllProcessDefs();
		// 遍历所有流程定义
		for (ProcessDefinition pDef : pList) {
			proIds.add(pDef.getId());
		}

		return proIds;
	}

	// 获取所有流程定义的名称
	public List<String> getAllProcessDefName() {

		List<String> proNameList = new ArrayList<String>();

		List<ProcessDefinition> pList = getAllProcessDefs();
		// 遍历所有流程定义
		for (ProcessDefinition pDef : pList) {
			proNameList.add(pDef.getName());
		}

		return proNameList;
	}

	// 获取流程定义及对应流程名称
	public Map<String, String> getAllProcessDefIdAndName() {

		Map<String, String> proIdNameMap = new HashMap<String, String>();

		List<ProcessDefinition> pList = getAllProcessDefs();
		// 遍历所有流程定义
		for (ProcessDefinition pDef : pList) {
			proIdNameMap.put(pDef.getId(), pDef.getName());
		}

		return proIdNameMap;
	}

	// 获取用户可触发的流程定义及对应流程名称
	public Map<String, String> getAllProcessDefIdAndNameByUserName(
			String userName) {

		Map<String, String> proIdNameMap = new HashMap<String, String>();

		List<ProcessDefinition> pList = getAllProcessDefsByUserName(userName);
		// 遍历所有流程定义
		for (ProcessDefinition pDef : pList) {
			proIdNameMap.put(pDef.getId(), pDef.getName());
		}

		return proIdNameMap;
	}
  
	// 根据可以执行该流程的组的信息，找到相应的流程
	public Map<String, String> getAllProcessDefIdAndNameByGroupName(
			String groupName) {

		Map<String, String> proIdNameMap = new HashMap<String, String>();

		List<ProcessDefinition> pList = getAllProcessDefsByGroupName(groupName);
		// 遍历所有流程定义
		for (ProcessDefinition pDef : pList) {
			proIdNameMap.put(pDef.getId(), pDef.getName());
		}

		return proIdNameMap;
	}

	

	// 获取任务及对应的流程实例id
	public Map<Task, String> getAllTasksAndProcessIdByCandidateUser(
			String userName) {

		Map<Task, String> taskProMap = new HashMap<Task, String>();

		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.taskCandidateUser(userName)
				.orderByTaskCreateTime().desc().list();

		// 遍历所有流程定义
		for (Task task : taskList) {
			taskProMap.put(task, task.getProcessInstanceId());
		}

		return taskProMap;
	}

	// 根据过程Id和用户名返回对应的task
	public Task getTaskIdByProcessIdAndCandidateUser(String processId,
			String userName) {

		TaskQuery taskQuery = taskService.createTaskQuery();

		List<Task> taskList = taskQuery.taskCandidateUser(userName)
				.orderByTaskCreateTime().desc().list();

		// 遍历所有流程定义
		for (Task task : taskList) {

			String proId = task.getProcessDefinitionId();
			if (processId.equals(proId))
				return task;
		}

		return null;
	}

	// 根据过程Id和用户名以及用户组返回对应的task
	public Task getTaskIdByProcessId(String processId) {

		TaskQuery taskQuery = taskService.createTaskQuery();

		List<Task> taskList = taskQuery.processDefinitionId(processId).list();

		// 遍历所有流程定义
		for (Task task : taskList) {

			String proId = task.getProcessDefinitionId();
			if (processId.equals(proId))
				return task;
		}

		return null;
	}

	// 根据taskId获取任务的from表单属性信息
	public List<FormProperty> getFormpropertyByTaskId(String taskId) {

		TaskFormData taskFormData = formService.getTaskFormData(taskId);

		return taskFormData.getFormProperties();
	}

	/**
	 * 完成任务，提交数据
	 * 
	 * @param taskId
	 * @param variables
	 */
	public void completeTask(String taskId, Map<String, Object> variables) {
		if (variables == null)
			variables = new HashMap<String, Object>();

		taskService.complete(taskId, variables);
	}

	// 提交form信息完成task
	public void submitTaskByFormData(String taskId,
			Map<String, String> properties) {

		formService.submitTaskFormData(taskId, properties);
	}
   

	/************************************************ 流程查询 **********************************************/

	public int queryProcessInstanceCountByProcessDefId(String processDefId) {

		int count = 0;

		List<ProcessInstance> proInsList = runtimeService
				.createProcessInstanceQuery().processDefinitionId(processDefId)
				.list();

		// 判定流程实例是否已经完成，获取未完成的流程实例数目
		for (ProcessInstance proIns : proInsList) {
			if (!proIns.isEnded())
				count++;
		}

		return count;
	}

	/**
	 * 通过流程定义Id获取所有活动的流程实例
	 * 
	 * @param processDefId
	 * @return
	 */
	public List<ProcessInstance> queryAllActivityProcessInstanceByProcessDefId(
			String processDefId) {
		List<ProcessInstance> proInsList = runtimeService
				.createProcessInstanceQuery().processDefinitionId(processDefId)
				.list();

		// 判定流程实例是否已经完成，获取未完成的流程实例数目
		for (ProcessInstance proIns : proInsList) {
			if (proIns.isEnded())
				proInsList.remove(proIns);
		}

		return proInsList;
	}

	/**
	 * 通过流程定义Id获取单实例流程Id
	 * 
	 * @param processDefId
	 * @return
	 */
	public String getSingleProcessInstanceByProcessDefId(String processDefId) {

		List<ProcessInstance> proInsList = runtimeService
				.createProcessInstanceQuery().processDefinitionId(processDefId)
				.list();

		// 判定流程实例是否已经完成，获取未完成的流程实例数目
		for (ProcessInstance proIns : proInsList) {
			if (!proIns.isEnded())
				return proIns.getId();
		}

		return null;
	}
	//开启JobExecutor
     public void startJobExecutor(){
	 processEngine.getProcessEngineConfiguration().getJobExecutor().start();
 }
	
	/************************************************ 部署流程定义 **********************************************/

	// 部署单个流程定义
	public void deployProcessDef(String filePath) {

		repositoryService.createDeployment().addClasspathResource(filePath)
				.deploy();
	}

	// 部署多个流程zip包
	public void deployProcessDefsZip(String filePath) {

		// ？？？？？？？？？？？？？？？？？？？？？？？？？待处理
		repositoryService.createDeployment().addClasspathResource(filePath)
				.deploy();
	}

	/************************************************ 获取配置数据 **********************************************/

	/**
	 * 根据流程定义ID获取流程实例属性 Instance 多人单实例为小写s Instance 多人多实例为小写m Single 单人单实例小写s
	 * Single 单人多实例小写m
	 * 
	 * @param processDefId
	 * @return 流程单多实例属性
	 */
	public Map<String, String> getInstanceTypeOfProcessByProcessDefId(
			String processDefId) {

		Map<String, String> insType = new HashMap<String, String>();

		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefId);
		System.out.println("流程定义ID:" + processDefId);

		// 根据流程获取相应数据
		Map<String, Object> proProperties = def.getVariables();

		insType.put("Instance", (String) proProperties.get("Instance"));
		insType.put("Single", (String) proProperties.get("Single"));
		insType.put("MutiRecover", (String) proProperties.get("MutiRecover"));

		return insType;
	}

	

	/**
	 * 根据流程定义Id启动流程
	 * 
	 * @param processId
	 *            userId
	 * @return
	 */
	public ProcessInstance startProcessByProcessId(String processId,
			String userId) {

		Map<String, Object> variables = new HashMap<String, Object>();

		// 判定是否存在creator设置
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processId);
		// 根据流程获取相应数据
		Map<String, Object> proProperties = def.getVariables();

		String creator = (String) proProperties.get("ProcessCreator");
		// List<String> userList = new ArrayList<String>();
		// userList.add(creator);
		if (creator != null && "y".equals(creator))
			variables.put("ProcessCreator", userId);
		this.startJobExecutor();
		return runtimeService.startProcessInstanceById(processId, variables);
	}

	public List<FormProperty> getFormPropertiesByTaskId(String taskId) {
		TaskFormData taskFormData = formService.getTaskFormData(taskId);
		List<FormProperty> taskFormProperties = taskFormData
				.getFormProperties();
		return taskFormProperties;
	}

	// 根据taskName和taskId构建task对应的form表单html代码,并根据processId构建按钮
	public String getFormHtmlCodeByTaskNameAndId(String processId,
			String taskName, String taskId) {

		// 用该用独立的html代码生成类对象来处理该处Html代码生成逻辑，并需不断完善
		String innerHtml = "";

		innerHtml += "<script language=\"javascript\">"
				+ "function cancel() {"
				+ "var cancelDiv = document.createElement(\"div\");"
				+ "ques.setAttribute(\"id\", \"cancel\");"
				+ "cancelDiv.innerHTML = \"<input type='hidden' name='cancel' value='true'/>\";}"
				+ "</script>";

		innerHtml += "<script language=\"javascript\">"
				+ "function OK() {"
				+ "var cancelDiv = document.createElement(\"div\");"
				+ "ques.setAttribute(\"id\", \"cancel\");"
				+ "cancelDiv.innerHTML = \"<input type='hidden' name='cancel' value='false'/>\";}"
				+ "</script>";

		innerHtml += "<h1>" + taskName + "</h1>";

		TaskFormData taskFormData = formService.getTaskFormData(taskId);

		List<FormProperty> taskFormProperties = taskFormData
				.getFormProperties();

		// form表单生成
		for (FormProperty formProperty : taskFormProperties) {
			FormType type = formProperty.getType();

			if (type == null) {
				innerHtml += formProperty.getName() + ":<br>"
						+ "<input type=\"text\" name=\"" + formProperty.getId()
						+ "\" value=\"" + formProperty.getValue() + "\" />"
						+ "<br>";
			} else if (type.getName().equals("date")) {
				innerHtml += formProperty.getName() + ":<br>"
						+ "<input type=\"text\" name=\"" + formProperty.getId()
						+ "\" value=\"" + formProperty.getValue() + "\" />"
						+ "<br>";
			}
		}

		// 提交按钮
		innerHtml += "<input type=\"hidden\" name=\"processId\" value=\""
				+ processId + "\"/>";
		innerHtml += "<input type=\"hidden\" name=\"taskId\" value=\"" + taskId
				+ "\"/>";
		innerHtml += "<button type=\"submit\" name=\"cancel\" onclick=\"OK()\">提交</button> "
				+ "<button type=\"reset\">重置</button> "
				+ "<button type=\"submit\" name=\"cancel\" onclick=\"cancel()\">取消</button>";

		return innerHtml;
	}

	// 根据过程定义中的user定义判定是否为合法用户
	public boolean isValidUser(String user, String proUser) {
		// 拆分过程定义中的users
		if (proUser == null) {
			return false;
		}

		String[] userList = proUser.split(";");

		for (String userItem : userList) {
			if (userItem.equals(user))
				return true;
		}

		return false;
	}

	// 根据过程定义中的Group定义判定是否为合法用户
	public boolean isValidGroupUser(List<String> gList, String group) {

		// 数据库查询group对于的用户组
		String[] groupList = group.split(";");
		// 迭代组
		for (String gName : gList) {
			for (String userItem : groupList) {
				if (userItem.equals(gName))
					return true;
			}
		}
		return false;
	}

	/**
	 * 返回任务页面跳转描述信息
	 * 
	 * @param taskId
	 * @return
	 */
	public String getTaskDocumentInfo(String taskId) {

		return taskService.createTaskQuery().taskId(taskId).singleResult()
				.getDescription().trim();
	}

	/**
	 * 目用task的document来描述需加载的页面和任务完成时需要跳转的页面 页面描述用分号隔开举例：
	 * load:discussion;jump:TaskExeSuc
	 * 
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getTaskDocumentParseredInfo(String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

		String docInfo = task.getDescription().trim();

		// 命令+类名,如果存在很多数据需拆分并迭代迭代添加到map中
		Map<String, String> comInfo = new HashMap<String, String>();

		String[] docItem = docInfo.split(";");

		for (String term : docItem) {

			// 判定是否为空
			if (term.equals("") || term.isEmpty())
				continue;

			String[] tList = term.split(":");
			comInfo.put(tList[0], tList[1]);
		}

		return comInfo;
	}

	/**
	 * 取回流程
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            取回节点ID
	 * @throws Exception
	 */
	public void callBackProcess(String taskId, String activityId)
			throws Exception {
		if (activityId.isEmpty()) {
			throw new Exception("目标节点ID为空！");
		}

		// 查找所有并行任务节点，同时取回
		List<Task> taskList = findTaskListByKey(
				findProcessInstanceByTaskId(taskId).getId(),
				findTaskById(taskId).getTaskDefinitionKey());
		for (Task task : taskList) {
			commitProcess(task.getId(), null, activityId);
		}
	}

	/**
	 * 中止流程(特权人直接审批通过等)
	 * 
	 * @param taskId
	 */
	public void endProcess(String taskId) throws Exception {
		ActivityImpl endActivity = findActivitiImpl(taskId, "end");
		commitProcess(taskId, null, endActivity.getId());
	}

	public void deleteProcessIns(String proInsId){
		runtimeService.deleteProcessInstance(proInsId, "delete");
	}
	/**
	 * 完成任务(强制完成)
	 * 
	 * @param taskId
	 */
	public void completetask(String taskId) throws Exception {
		taskService.complete(taskId);
	}

	/**
	 * 删除流程
	 * 
	 * @param ProcessinsId
	 *            实例Id
	 * @param reason
	 *            删除原因
	 */
	public void deleteProcessins(String processinsId, String reason) {
		runtimeService.deleteProcessInstance(processinsId, reason);
	}

	/**
	 * 转办流程
	 * 
	 * @param taskId
	 *            当前任务节点ID
	 * @param userCode
	 *            被转办人Code
	 */
	public void transferAssignee(String taskId, String userCode) {
		taskService.setAssignee(taskId, userCode);
	}

	/**
	 * *************************************************************************
	 * *************************************************************************
	 * *<br>
	 * ************************************************以下为流程转向操作核心逻辑************
	 * ******************************************************************<br>
	 * *************************************************************************
	 * *************************************************************************
	 * *<br>
	 */

	/**
	 * @param taskId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param activityId
	 *            流程转向执行任务节点ID<br>
	 *            此参数为空，默认为提交操作
	 * @throws Exception
	 */
	private void commitProcess(String taskId, Map<String, Object> variables,
			String activityId) throws Exception {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		// 跳转节点为空，默认提交操作
		if (activityId.isEmpty()) {
			taskService.complete(taskId, variables);
		} else {// 流程转向操作
			turnTransition(taskId, activityId, variables);
		}
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl,
			List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	private void turnTransition(String taskId, String activityId,
			Map<String, Object> variables) throws Exception {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向任务
		taskService.complete(taskId, variables);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	/**
	 * *************************************************************************
	 * *************************************************************************
	 * *<br>
	 * ************************************************以下为查询流程驳回节点核心逻辑**********
	 * *****************************************************************<br>
	 * *************************************************************************
	 * *************************************************************************
	 * *<br>
	 */

	/**
	 * 迭代循环流程树结构，查询当前节点可驳回的任务节点
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param currActivity
	 *            当前活动节点
	 * @param rtnList
	 *            存储回退节点集合
	 * @param tempList
	 *            临时存储节点集合（存储一次迭代过程中的同级userTask节点）
	 * @return 回退节点集合
	 */
	private List<ActivityImpl> iteratorBackActivity(String taskId,
			ActivityImpl currActivity, List<ActivityImpl> rtnList,
			List<ActivityImpl> tempList) throws Exception {
		// 查询流程定义，生成流程树结构
		ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);

		// 当前节点的流入来源
		List<PvmTransition> incomingTransitions = currActivity
				.getIncomingTransitions();
		// 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点
		List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>();
		// 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点
		List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();
		// 遍历当前节点所有流入路径
		for (PvmTransition pvmTransition : incomingTransitions) {
			TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
			ActivityImpl activityImpl = transitionImpl.getSource();
			String type = (String) activityImpl.getProperty("type");
			/**
			 * 并行节点配置要求：<br>
			 * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)
			 */
			if ("parallelGateway".equals(type)) {// 并行路线
				String gatewayId = activityImpl.getId();
				String gatewayType = gatewayId.substring(gatewayId
						.lastIndexOf("_") + 1);
				if ("START".equals(gatewayType.toUpperCase())) {// 并行起点，停止递归
					return rtnList;
				} else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
					parallelGateways.add(activityImpl);
				}
			} else if ("startEvent".equals(type)) {// 开始节点，停止递归
				return rtnList;
			} else if ("userTask".equals(type)) {// 用户任务
				tempList.add(activityImpl);
			} else if ("exclusiveGateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
				currActivity = transitionImpl.getSource();
				exclusiveGateways.add(currActivity);
			}
		}

		/**
		 * 迭代条件分支集合，查询对应的userTask节点
		 */
		for (ActivityImpl activityImpl : exclusiveGateways) {
			iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
		}

		/**
		 * 迭代并行集合，查询对应的userTask节点
		 */
		for (ActivityImpl activityImpl : parallelGateways) {
			iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
		}

		/**
		 * 根据同级userTask集合，过滤最近发生的节点
		 */
		currActivity = filterNewestActivity(processInstance, tempList);
		if (currActivity != null) {
			// 查询当前节点的流向是否为并行终点，并获取并行起点ID
			String id = findParallelGatewayId(currActivity);
			if (id.isEmpty()) {// 并行起点ID为空，此节点流向不是并行终点，符合驳回条件，存储此节点
				rtnList.add(currActivity);
			} else {// 根据并行起点ID查询当前节点，然后迭代查询其对应的userTask任务节点
				currActivity = findActivitiImpl(taskId, id);
			}

			// 清空本次迭代临时集合
			tempList.clear();
			// 执行下次迭代
			iteratorBackActivity(taskId, currActivity, rtnList, tempList);
		}
		return rtnList;
	}

	/**
	 * 反向排序list集合，便于驳回节点按顺序显示
	 * 
	 * @param list
	 * @return
	 */
	private List<ActivityImpl> reverList(List<ActivityImpl> list) {
		List<ActivityImpl> rtnList = new ArrayList<ActivityImpl>();
		// 由于迭代出现重复数据，排除重复
		for (int i = list.size(); i > 0; i--) {
			if (!rtnList.contains(list.get(i - 1)))
				rtnList.add(list.get(i - 1));
		}
		return rtnList;
	}

	/**
	 * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID
	 * 
	 * @param activityImpl
	 *            当前节点
	 * @return
	 */
	private String findParallelGatewayId(ActivityImpl activityImpl) {
		List<PvmTransition> incomingTransitions = activityImpl
				.getOutgoingTransitions();
		for (PvmTransition pvmTransition : incomingTransitions) {
			TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
			activityImpl = transitionImpl.getDestination();
			String type = (String) activityImpl.getProperty("type");
			if ("parallelGateway".equals(type)) {// 并行路线
				String gatewayId = activityImpl.getId();
				String gatewayType = gatewayId.substring(gatewayId
						.lastIndexOf("_") + 1);
				if ("END".equals(gatewayType.toUpperCase())) {
					return gatewayId.substring(0, gatewayId.lastIndexOf("_"))
							+ "_start";
				}
			}
		}
		return null;
	}

	/**
	 * 根据流入任务集合，查询最近一次的流入任务节点
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param tempList
	 *            流入任务集合
	 * @return
	 */
	private ActivityImpl filterNewestActivity(ProcessInstance processInstance,
			List<ActivityImpl> tempList) {
		while (tempList.size() > 0) {
			ActivityImpl activity_1 = tempList.get(0);
			HistoricActivityInstance activityInstance_1 = findHistoricUserTask(
					processInstance, activity_1.getId());
			if (activityInstance_1 == null) {
				tempList.remove(activity_1);
				continue;
			}

			if (tempList.size() > 1) {
				ActivityImpl activity_2 = tempList.get(1);
				HistoricActivityInstance activityInstance_2 = findHistoricUserTask(
						processInstance, activity_2.getId());
				if (activityInstance_2 == null) {
					tempList.remove(activity_2);
					continue;
				}

				if (activityInstance_1.getEndTime().before(
						activityInstance_2.getEndTime())) {
					tempList.remove(activity_1);
				} else {
					tempList.remove(activity_2);
				}
			} else {
				break;
			}
		}
		if (tempList.size() > 0) {
			return tempList.get(0);
		}
		return null;
	}

	/**
	 * 查询指定任务节点的最新记录
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param activityId
	 * @return
	 */
	private HistoricActivityInstance findHistoricUserTask(
			ProcessInstance processInstance, String activityId) {
		HistoricActivityInstance rtnVal = null;
		// 查询当前流程实例审批结束的历史节点
		List<HistoricActivityInstance> historicActivityInstances = historyService
				.createHistoricActivityInstanceQuery().activityType("userTask")
				.processInstanceId(processInstance.getId())
				.activityId(activityId).finished()
				.orderByHistoricActivityInstanceEndTime().desc().list();
		if (historicActivityInstances.size() > 0) {
			rtnVal = historicActivityInstances.get(0);
		}

		return rtnVal;
	}

	/**
	 * *************************************************************************
	 * *******<br>
	 * **********************以下为根据 任务节点ID 获取流程各对象查询方法**********************<br>
	 * *************************************************************************
	 * ********<br>
	 */

	/**
	 * 根据任务ID获得任务实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private TaskEntity findTaskById(String taskId) throws Exception {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery()
				.taskId(taskId).singleResult();
		if (task == null) {
			throw new Exception("任务实例未找到!");
		}
		return task;
	}

	/**
	 * 根据流程实例ID和任务key值查询所有同级任务集合
	 * 
	 * @param processInstanceId
	 * @param key
	 * @return
	 */
	private List<Task> findTaskListByKey(String processInstanceId, String key) {
		return taskService.createTaskQuery()
				.processInstanceId(processInstanceId).taskDefinitionKey(key)
				.list();
	}

	/**
	 * 根据任务ID获取对应的流程实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private ProcessInstance findProcessInstanceByTaskId(String taskId)
			throws Exception {
		// 找到流程实例
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(findTaskById(taskId).getProcessInstanceId())
				.singleResult();
		if (processInstance == null) {
			throw new Exception("流程实例未找到!");
		}
		return processInstance;
	}

	/**
	 * 根据任务ID获取流程定义
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(
			String taskId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId)
						.getProcessDefinitionId());

		if (processDefinition == null) {
			throw new Exception("流程定义未找到!");
		}

		return processDefinition;
	}

	/**
	 * 根据任务ID和节点ID获取活动节点 <br>
	 * 
	 * @param taskId
	 *            任务ID
	 * @param activityId
	 *            活动节点ID <br>
	 *            如果为null或""，则默认查询当前活动节点 <br>
	 *            如果为"end"，则查询结束节点 <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	private ActivityImpl findActivitiImpl(String taskId, String activityId)
			throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

		// 获取当前活动节点ID
		if (activityId == null) {
			activityId = findTaskById(taskId).getTaskDefinitionKey();
		}

		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl
						.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)
				.findActivity(activityId);

		return activityImpl;
	}

	/**
	 * 根据流程实例Id获取流程变量variableName <br>
	 * 
	 * @param variableName
	 *            流程变量
	 * @param executionId
	 *            流程实例Id
	 * @return
	 * @throws Exception
	 */
	public Object getProcessvariables(String executionId, String variableName)
			throws Exception {
		Object ob = runtimeService.getVariable(executionId, variableName);
		return ob;

	}

	/**
	 * 根据流程实例Id获取设置变量variableName <br>
	 * 
	 * @param variableName
	 *            流程变量名字
	 * @param executionId
	 *            流程实例Id
	 * @param object
	 *            对象
	 * @throws Exception
	 */
	public void setProcessvariables(String executionId, String variableName,
			Object object) throws Exception {
		runtimeService.setVariable(executionId, variableName, object);
	}

	// 文件名存储
	public synchronized void setTaskProperty(String taskId,
			String variableName, Object value) {
		taskService.setVariable(taskId, variableName, value);
	}

	public synchronized Object getTaskProperty(String taskId,
			String variableName) {
		return taskService.getVariable(taskId, variableName);
	}

	public synchronized void removeTaskProperty(String taskId,
			String variableName) {
		taskService.removeVariable(taskId, variableName);
	}

	public synchronized void setProcessInsProperty(String processInsId,
			String variableName, Object value) {
		runtimeService.setVariable(processInsId, variableName, value);
	}
	public synchronized boolean hasProcessInsProperty(String processInsId,
			String variableName){
		return runtimeService.hasVariable(processInsId, variableName);
	}
	public synchronized boolean hasTaskInsProperty(String taskId,
			String variableName){
		return taskService.hasVariable(taskId, variableName);
	}
	public synchronized Object getProcessInsProperty(String processInsId,
			String variableName) {
		return runtimeService.getVariable(processInsId, variableName);
	}

	public synchronized void removeProcessInsProperty(String processInsId,
			String variableName) {
		runtimeService.removeVariable(processInsId, variableName);
	}
}

