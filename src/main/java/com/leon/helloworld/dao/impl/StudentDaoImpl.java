package com.leon.helloworld.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.leon.helloworld.dao.StudentDao;
import com.leon.helloworld.po.Student;
@Repository
@Transactional
public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao {

	public StudentDaoImpl(){
		super(Student.class);
	}

}
