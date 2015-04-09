package com.leon.helloworld.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.leon.helloworld.dao.BaseDao;
@Repository
@Transactional
public class BaseDaoImpl<T> implements BaseDao<T> {

	private Class<T> clazz;
	@Autowired
	private SessionFactory sf;
	public BaseDaoImpl(){}
	public BaseDaoImpl(Class<T> entityClass){
        this.clazz = entityClass;
	}
	
	@Override
	public Integer save(T t) {
		return (Integer)sf.getCurrentSession().save(t);
	}

	@Override
	public void delete(T t) {
		sf.getCurrentSession().delete(t);
	}

	@Override
	public void update(T t) {
		// TODO Auto-generated method stub
		sf.getCurrentSession().update(t);
	}

	@Override
	public T search(Integer id) {
		// TODO Auto-generated method stub
		return (T)sf.getCurrentSession().get(clazz, id);
	}

	@Override
	public T searchByProperties(String hql, Object[] params) {
		// TODO Auto-generated method stub
		if(hql == null){
			return null;
		}
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return (T) query.uniqueResult();
	}

	@Override
	public List<T> findByProperties(String hql, Object[] params) {
		// TODO Auto-generated method stub
		if(hql == null){
			return null;
		}
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.list();
	}

	@Override
	public List<T> findByProperties(String hql, Object[] params, Integer page,
			Integer size) {
		// TODO Auto-generated method stub
		if(hql == null){
			return null;
		}
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		query.setFirstResult((page-1)*size);
		query.setMaxResults(size);
		return query.list();
	}

	@Override
	public Long count(String hql, Object[] params) {
		// TODO Auto-generated method stub
		if(hql == null){
			return null;
		}
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return (Long) query.uniqueResult();
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return sf.getCurrentSession().createQuery("from "+clazz.getSimpleName()).list();
	}

	@Override
	public List<T> findAll(Integer page, Integer size) {
		// TODO Auto-generated method stub
		Query query = sf.getCurrentSession().createQuery("from"+clazz.getSimpleName());
		query.setFirstResult((page-1)*size).setMaxResults(size);
		return query.list();
	}

	@Override
	public Long countAll() {
		// TODO Auto-generated method stub
		return (Long) sf.getCurrentSession().createQuery("select count(*) from " + clazz.getSimpleName()).uniqueResult();
	}
	@Override
	public void executeHql(String hql) {
		// TODO Auto-generated method stub
		sf.getCurrentSession().createQuery(hql).executeUpdate();
	}
	@Override
	public void executeHql(String hql, Object[] params) {
		// TODO Auto-generated method stub
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		query.executeUpdate();
	}
	

}
