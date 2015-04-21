package com.leon.helloworld.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leon.helloworld.dao.StudentDao;
import com.leon.helloworld.po.Student;
import com.leon.helloworld.service.StudentService;
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao std;
	@Override
	public List<Student> getAllStudents() {
		// TODO Auto-generated method stub
		
		
		return std.findAll();
	}

}
