/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.bookmarks.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;

import com.liferay.portlet.bookmarks.model.BookmarksEntry;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class BookmarksEntryExportActionableDynamicQuery
	extends BookmarksEntryActionableDynamicQuery {
	public BookmarksEntryExportActionableDynamicQuery(
		PortletDataContext portletDataContext) throws SystemException {
		_portletDataContext = portletDataContext;

		setGroupId(_portletDataContext.getScopeGroupId());
	}

	@Override
	protected void addCriteria(DynamicQuery dynamicQuery) {
		_portletDataContext.addDateRangeCriteria(dynamicQuery, "modifiedDate");
	}

	@Override
	protected void performAction(Object object) throws PortalException {
		BookmarksEntry stagedModel = (BookmarksEntry)object;

		StagedModelDataHandlerUtil.exportStagedModel(_portletDataContext,
			stagedModel);
	}

	private PortletDataContext _portletDataContext;
}