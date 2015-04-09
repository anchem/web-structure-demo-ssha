package com.leon.helloworld.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.leon.helloworld.po.Student;
import com.leon.helloworld.process.ProcessManager;
import com.leon.helloworld.service.StudentService;

@Controller
public class HelloWorldController {

	@Autowired
	private Student student;
	@Autowired
	private ProcessManager pm;
	@Autowired
	private StudentService ss;

	/**
	 * check if the architecture works
	 * 
	 * @param request
	 * @param response
	 * @return check.jsp
	 * */
	@RequestMapping(value = "check.do")
	public String checkArchitecture(HttpServletRequest request, HttpServletResponse response) {
		String isWork = "working";
		request.setAttribute("isWork", isWork);
		List<Student> list = ss.getAllStudents();
		request.setAttribute("stuList", list);
		/*ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runs = engine.getRuntimeService();
		TaskService ts = engine.getTaskService();
		runs.startProcessInstanceByKey("testProcess");
		System.out.println("== process started ==");
		Task task = ts.createTaskQuery().singleResult();
		System.out.println("= first task is running =");
		ts.complete(task.getId());
		System.out.println("== first task is ended ==");
		task = ts.createTaskQuery().singleResult();
		System.out.println("== next task : " + task + " ==");
		request.setAttribute("taskInfo", "process is completed!");*/

		return "check";
	}
}
