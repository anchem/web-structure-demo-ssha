package com.leon.helloworld.dao;

import java.util.List;

public interface BaseDao<T> {

	Integer save(T t);
	void delete(T t);
	void update(T t);
	void executeHql(String hql);
	void executeHql(String hql,Object[] params);
	T search(Integer id);
	T searchByProperties(String hql,Object[] params);
	List<T> findByProperties(String hql,Object[] params);
	List<T> findByProperties(String hql,Object[] params,Integer page,Integer size);
	Long count(String hql,Object[] params);
	List<T> findAll();
	List<T> findAll(Integer page,Integer size);
	Long countAll();
}
