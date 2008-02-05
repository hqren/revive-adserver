/*
+---------------------------------------------------------------------------+
| Openads v2.5                                                              |
| ======${RELEASE_MAJOR_MINOR_DOUBLE_UNDERLINE}                                                                 |
|                                                                           |
| Copyright (c) 2003-2008 m3 Media Services Ltd                             |
| For contact details, see: http://www.openads.org/                         |
|                                                                           |
| This program is free software; you can redistribute it and/or modify      |
| it under the terms of the GNU General Public License as published by      |
| the Free Software Foundation; either version 2 of the License, or         |
| (at your option) any later version.                                       |
|                                                                           |
| This program is distributed in the hope that it will be useful,           |
| but WITHOUT ANY WARRANTY; without even the implied warranty of            |
| MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             |
| GNU General Public License for more details.                              |
|                                                                           |
| You should have received a copy of the GNU General Public License         |
| along with this program; if not, write to the Free Software               |
| Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA |
+---------------------------------------------------------------------------+
| Copyright (c) 2003-2008 m3 Media Services Ltd                             |
|                                                                           |
|  Licensed under the Apache License, Version 2.0 (the "License");          |
|  you may not use this file except in compliance with the License.         |
|  You may obtain a copy of the License at                                  |
|                                                                           |
|    http://www.apache.org/licenses/LICENSE-2.0                             |
|                                                                           |
|  Unless required by applicable law or agreed to in writing, software      |
|  distributed under the License is distributed on an "AS IS" BASIS,        |
|  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. |
|  See the License for the specific language governing permissions and      |
|  limitations under the License.                                           |
+---------------------------------------------------------------------------+
$Id:$
*/

package org.openads.agency;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.openads.config.GlobalSettings;
import org.openads.utils.ErrorMessage;
import org.openads.utils.TextUtils;

/**
 * Verify Modify Agency method
 * 
 * @author     Andriy Petlyovanyy <apetlyovanyy@lohika.com>
 */
public class TestModifyAgency extends AgencyTestCase {
	private Integer agencyId;

	protected void setUp() throws Exception {
		super.setUp();
		agencyId = createAgency();
	}

	protected void tearDown() throws Exception {
		deleteAgency(agencyId);
		super.tearDown();
	}

	/**
	 * Test method with all required fields and some optional.
	 * 
	 * @throws XmlRpcException
	 */
	public void testModifyAgencyAllReqAndSomeOptionalFields()
			throws XmlRpcException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(CONTACT_NAME, "agencyContact");
		struct.put(AGENCY_NAME, "newAgency");
		struct.put(EMAIL_ADDRESS, "test@mail.com");
		Object[] params = new Object[] { sessionId, struct };
		final boolean result = (Boolean) client.execute(MODIFY_AGENCY_METHOD,
				params);
		assertTrue(result);
	}

	/**
	 * Test method without some required fields.
	 * 
	 * @throws Exception
	 */
	public void testModifyAgencyWithoutSomeRequiredFields() throws Exception {
		Map<String, String> struct = new HashMap<String, String>();
		struct.put(CONTACT_NAME, "agencyContact");
		struct.put(AGENCY_NAME, "testAgency");
		struct.put(EMAIL_ADDRESS, "test@mail.com");
		Object[] params = new Object[] { sessionId, struct };

		try {
			client.execute(MODIFY_AGENCY_METHOD, params);
			fail("Agency modified without required 'agencyId' parameter");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, ErrorMessage
					.getMessage(
							ErrorMessage.FIELD_IN_STRUCTURE_DOES_NOT_EXISTS,
							AGENCY_ID), e.getMessage());
		}
	}

	/**
	 * Execute test method with error
	 * 
	 * @param params -
	 *            parameters for test method
	 * @param errorMsg -
	 *            true error messages
	 * @throws MalformedURLException
	 */
	private void executeModifyAgencyWithError(Object[] params, String errorMsg)
			throws MalformedURLException {

		try {
			execute(MODIFY_AGENCY_METHOD, params);
			fail(MODIFY_AGENCY_METHOD
					+ " executed successfully, but it shouldn't.");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, errorMsg, e
					.getMessage());
		}
	}

	/**
	 * Test method with fields that has value greater than max.
	 * 
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testModifyAgencyGreaterThanMaxFieldValueError()
			throws MalformedURLException, XmlRpcException {

		final String strGreaterThan255 = TextUtils.getString(256);
		final String strGreaterThan64 = TextUtils.getString(65);

		assertNotNull(agencyId);

		Map<String, Object> struct = new HashMap<String, Object>();
		Object[] params = new Object[] { sessionId, struct };
		struct.put(AGENCY_ID, agencyId);

		// test advertiserName
		struct.put(AGENCY_NAME, strGreaterThan255);
		executeModifyAgencyWithError(params, ErrorMessage.getMessage(
				ErrorMessage.EXCEED_MAXIMUM_LENGTH_OF_FIELD, AGENCY_NAME));

		// test contactName
		struct.remove(AGENCY_NAME);
		struct.put(CONTACT_NAME, strGreaterThan255);
		executeModifyAgencyWithError(params, ErrorMessage.getMessage(
				ErrorMessage.EXCEED_MAXIMUM_LENGTH_OF_FIELD, CONTACT_NAME));

		// test username
		struct.remove(CONTACT_NAME);
		struct.put(USERNAME, strGreaterThan64);
		executeModifyAgencyWithError(params, ErrorMessage.getMessage(
				ErrorMessage.EXCEED_MAXIMUM_LENGTH_OF_FIELD, USERNAME));

		// test password
		struct.remove(USERNAME);
		struct.put(PASSWORD, strGreaterThan64);
		executeModifyAgencyWithError(params, ErrorMessage.getMessage(
				ErrorMessage.EXCEED_MAXIMUM_LENGTH_OF_FIELD, PASSWORD));
	}

	/**
	 * Test method with fields that has value less than min
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyLessThanMinFieldValueError()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(USERNAME, "");

		try {

			client.execute(MODIFY_AGENCY_METHOD, new Object[] { sessionId,
					struct });
			fail("Agency modified, but must not");

			client.execute(MODIFY_AGENCY_METHOD, new Object[] { sessionId,
					struct });
			fail("Agency modified, but shouldn't have");

		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, ErrorMessage
					.getMessage(ErrorMessage.USERNAME_IS_FEWER_THAN, "1"), e
					.getMessage());

		}

		// test emailAddress
		struct.remove(USERNAME);
		struct.put(EMAIL_ADDRESS, "");
		try {
			client.execute(MODIFY_AGENCY_METHOD, new Object[] { sessionId,
					struct });
			fail(ErrorMessage.METHOD_EXECUTED_SUCCESSFULLY_BUT_SHOULD_NOT_HAVE);
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.EMAIL_IS_NOT_VALID, e.getMessage());
		}
	}

	/**
	 * Test method with fields that has min. allowed values.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyMinValues() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(AGENCY_NAME, "");
		struct.put(CONTACT_NAME, "");
		struct.put(EMAIL_ADDRESS, TextUtils.MIN_ALLOWED_EMAIL);
		struct.put(USERNAME, "s");
		struct.put(PASSWORD, "");
		Object[] params = new Object[] { sessionId, struct };
		final Boolean result = (Boolean) client.execute(MODIFY_AGENCY_METHOD,
				params);
		assertTrue(result);
	}

	/**
	 * Test method with fields that has max. allowed values.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyMaxValues() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(AGENCY_NAME, TextUtils.getString(255));
		struct.put(CONTACT_NAME, TextUtils.getString(255));
		struct.put(EMAIL_ADDRESS, TextUtils.getString(55) + "@mail.com");
		struct.put(USERNAME, TextUtils.generateUniqueString(64));
		struct.put(PASSWORD, TextUtils.getString(64));
		Object[] params = new Object[] { sessionId, struct };
		final Boolean result = (Boolean) client.execute(MODIFY_AGENCY_METHOD,
				params);
		assertTrue(result);
	}

	/**
	 * Try to modify agency with unknown id
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyUnknownIdError() throws XmlRpcException,
			MalformedURLException {
		final Integer id = createAgency();
		deleteAgency(id);
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, id);
		Object[] params = new Object[] { sessionId, struct };

		try {
			client.execute(MODIFY_AGENCY_METHOD, params);
			fail("Agency modified but shouldn't have");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, ErrorMessage
					.getMessage(ErrorMessage.UNKNOWN_ID_ERROR, "agencyId"), e
					.getMessage());
		}
	}

	/**
	 * Try to modify agency with the same username as an existing admin, agency,
	 * advertiser, or publisher username.
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyDuplicateUsernameError()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(USERNAME, GlobalSettings.getUserName());
		Object[] params = new Object[] { sessionId, struct };

		try {
			client.execute(MODIFY_AGENCY_METHOD, params);
			fail("Agency modified but shouldn't have");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE,
					ErrorMessage.USERNAME_MUST_BE_UNIQUE, e.getMessage());
		}
	}

	/**
	 * Try to modify agency with username fewer than 1 character.
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyUsernameFormatError1()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(USERNAME, "");
		Object[] params = new Object[] { sessionId, struct };

		try {
			client.execute(MODIFY_AGENCY_METHOD, params);
			fail("Agency modified but shouldn't have");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, ErrorMessage
					.getMessage(ErrorMessage.USERNAME_IS_FEWER_THAN, "1"), e
					.getMessage());
		}
	}

	/**
	 * Try to modify agency when the username is null and the password is not.
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyUsernameFormatError2()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(PASSWORD, "password");
		Object[] params = new Object[] { sessionId, struct };

		try {
			client.execute(MODIFY_AGENCY_METHOD, params);
			fail("Agency modified but shouldn't have");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE,
					ErrorMessage.USERNAME_IS_NULL_AND_THE_PASSWORD_IS_NOT, e
							.getMessage());
		}
	}

	/**
	 * Try to modify agency when there is '\' character in the password.
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyPasswordFormatError()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(AGENCY_ID, agencyId);
		struct.put(USERNAME, TextUtils.generateUniqueName("agencyUser"));
		struct.put(PASSWORD, "password\\");
		Object[] params = new Object[] { sessionId, struct };
		try {
			client.execute(MODIFY_AGENCY_METHOD, params);
			fail("Agency modified but shouldn't have");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, ErrorMessage
					.getMessage(ErrorMessage.PASSWORDS_CANNOT_CONTAIN, "\\"), e
					.getMessage());
		}
	}

	/**
	 * Test method with fields that has value of wrong type (error).
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyAgencyWrongTypeError() throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		Object[] params = new Object[] { sessionId, struct };

		struct.put(AGENCY_ID, TextUtils.NOT_INTEGER);
		executeModifyAgencyWithError(params, ErrorMessage.getMessage(
				ErrorMessage.FIELD_IS_NOT_INTEGER, AGENCY_ID));

		struct.put(AGENCY_ID, agencyId);
		struct.put(AGENCY_NAME, TextUtils.NOT_STRING);
		executeModifyAgencyWithError(params, ErrorMessage.getMessage(
				ErrorMessage.FIELD_IS_NOT_STRING, AGENCY_NAME));
	}
}
