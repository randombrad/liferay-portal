/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchPortletException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.impl.PortletModelImpl;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PropsValues;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class PortletPersistenceTest {
	@Before
	public void setUp() throws Exception {
		_persistence = (PortletPersistence)PortalBeanLocatorUtil.locate(PortletPersistence.class.getName());
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Portlet portlet = _persistence.create(pk);

		Assert.assertNotNull(portlet);

		Assert.assertEquals(portlet.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Portlet newPortlet = addPortlet();

		_persistence.remove(newPortlet);

		Portlet existingPortlet = _persistence.fetchByPrimaryKey(newPortlet.getPrimaryKey());

		Assert.assertNull(existingPortlet);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addPortlet();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Portlet newPortlet = _persistence.create(pk);

		newPortlet.setCompanyId(ServiceTestUtil.nextLong());

		newPortlet.setPortletId(ServiceTestUtil.randomString());

		newPortlet.setRoles(ServiceTestUtil.randomString());

		newPortlet.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(newPortlet, false);

		Portlet existingPortlet = _persistence.findByPrimaryKey(newPortlet.getPrimaryKey());

		Assert.assertEquals(existingPortlet.getId(), newPortlet.getId());
		Assert.assertEquals(existingPortlet.getCompanyId(),
			newPortlet.getCompanyId());
		Assert.assertEquals(existingPortlet.getPortletId(),
			newPortlet.getPortletId());
		Assert.assertEquals(existingPortlet.getRoles(), newPortlet.getRoles());
		Assert.assertEquals(existingPortlet.getActive(), newPortlet.getActive());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Portlet newPortlet = addPortlet();

		Portlet existingPortlet = _persistence.findByPrimaryKey(newPortlet.getPrimaryKey());

		Assert.assertEquals(existingPortlet, newPortlet);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchPortletException");
		}
		catch (NoSuchPortletException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Portlet newPortlet = addPortlet();

		Portlet existingPortlet = _persistence.fetchByPrimaryKey(newPortlet.getPrimaryKey());

		Assert.assertEquals(existingPortlet, newPortlet);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Portlet missingPortlet = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingPortlet);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Portlet newPortlet = addPortlet();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Portlet.class,
				Portlet.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("id", newPortlet.getId()));

		List<Portlet> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Portlet existingPortlet = result.get(0);

		Assert.assertEquals(existingPortlet, newPortlet);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Portlet.class,
				Portlet.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("id",
				ServiceTestUtil.nextLong()));

		List<Portlet> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Portlet newPortlet = addPortlet();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Portlet.class,
				Portlet.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("id"));

		Object newId = newPortlet.getId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("id", new Object[] { newId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingId = result.get(0);

		Assert.assertEquals(existingId, newId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Portlet.class,
				Portlet.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("id"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("id",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		Portlet newPortlet = addPortlet();

		_persistence.clearCache();

		PortletModelImpl existingPortletModelImpl = (PortletModelImpl)_persistence.findByPrimaryKey(newPortlet.getPrimaryKey());

		Assert.assertEquals(existingPortletModelImpl.getCompanyId(),
			existingPortletModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(
				existingPortletModelImpl.getPortletId(),
				existingPortletModelImpl.getOriginalPortletId()));
	}

	protected Portlet addPortlet() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Portlet portlet = _persistence.create(pk);

		portlet.setCompanyId(ServiceTestUtil.nextLong());

		portlet.setPortletId(ServiceTestUtil.randomString());

		portlet.setRoles(ServiceTestUtil.randomString());

		portlet.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(portlet, false);

		return portlet;
	}

	private PortletPersistence _persistence;
}