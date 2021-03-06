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

package com.liferay.portal.oauth;

import com.liferay.portal.kernel.oauth.OAuthException;
import com.liferay.portal.kernel.oauth.OAuthRequest;
import com.liferay.portal.kernel.oauth.OAuthResponse;
import com.liferay.portal.kernel.oauth.Verb;

/**
 * @author Brian Wing Shun Chan
 */
public class OAuthRequestImpl implements OAuthRequest {

	public OAuthRequestImpl(org.scribe.model.OAuthRequest oAuthRequest) {
		_oAuthRequest = oAuthRequest;
	}

	public void addBodyParameter(String key, String value) {
		_oAuthRequest.addBodyParameter(key, value);
	}

	public String getURL() {
		return _oAuthRequest.getUrl();
	}

	public Verb getVerb() {
		return VerbTranslator.translate(_oAuthRequest.getVerb());
	}

	public Object getWrappedOAuthRequest() {
		return _oAuthRequest;
	}

	public OAuthResponse send() throws OAuthException {
		try {
			return new OAuthResponseImpl(_oAuthRequest.send());
		}
		catch (Exception e) {
			throw new OAuthException(e);
		}
	}

	private org.scribe.model.OAuthRequest _oAuthRequest;

}