/*
 * Copyright (C) 2021 Vitalijus Dobrovolskis
 *
 * This file is part of commexp.
 *
 * commexp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the License.
 *
 * commexp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with commexp; see the file LICENSE. If not,
 * see <https://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

import {TestBed} from '@angular/core/testing';

import {GroupSessionService} from './group.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
describe('GroupSessionService', () => {
	let service: GroupSessionService;

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports: [HttpClientTestingModule]
		});
		service = TestBed.inject(GroupSessionService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
