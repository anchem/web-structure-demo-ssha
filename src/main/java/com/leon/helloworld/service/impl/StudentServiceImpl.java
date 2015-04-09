package com.leon.helloworld.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leon.helloworld.dao.BaseDao;
import com.leon.helloworld.dao.StudentDao;
import com.leon.helloworld.po.Student;
import com.leon.helloworld.service.StudentService;
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao std;
	@Override
	public List<Student> getAllStudents() {
		// TODO Auto-generated method stub
		/*System.out.println("search:"+std.search(new Integer(1)).getName());
		String hql = "from Student as s where s.name=?";
		Object[] params = {"leon"};
		System.out.println("searchByProp:"+std.searchByProperties(hql, params).getName());
		hql = "from Student as s where s.id<?";
		Object[] param = {10};
		System.out.println("findByProperties:"+std.findByProperties(hql, param, 1, 1));
		hql = "select count(*) from Student as s where s.id<?";
		System.out.println("count:"+std.count(hql, param));
		System.out.println("countAll:"+std.countAll());*/
		Student t = new Student();
		t.setId(3);
		/*Integer id = std.save(t);
		System.out.println("save:"+id);
		t.setId(id);
		System.out.println("search:"+std.search(id).getName());*/
		Object[] pa = {"llss",3};
		std.executeHql("update Student as s set s.className=? where s.id=?",pa);
		System.out.println("update:"+std.search(t.getId()).getClassName());
		return std.findAll();
	}

}
