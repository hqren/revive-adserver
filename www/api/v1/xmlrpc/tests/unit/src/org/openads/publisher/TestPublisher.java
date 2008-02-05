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

package org.openads.publisher;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Run all Publisher service tests
 * 
 * @author     Andriy Petlyovanyy <apetlyovanyy@lohika.com>
 */
public class TestPublisher {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.openads.publisher");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestPublisherCampaignStatistics.class);
		suite.addTestSuite(TestAddPublisher.class);
		suite.addTestSuite(TestPublisherZoneStatistics.class);
		suite.addTestSuite(TestPublisherBannerStatistics.class);
		suite.addTestSuite(TestPublisherAdvertiserStatistics.class);
		suite.addTestSuite(TestModifyPublisher.class);
		suite.addTestSuite(TestDeletePublisher.class);
		suite.addTestSuite(TestPublisherDailyStatistics.class);
		suite.addTestSuite(TestGetPublisher.class);
		suite.addTestSuite(TestGetPublisherListByAgencyId.class);
		// $JUnit-END$
		return suite;
	}

}
