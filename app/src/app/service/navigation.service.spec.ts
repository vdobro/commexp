/*
 * Copyright (C) 2020 Vitalijus Dobrovolskis
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

import {NavigationService} from './navigation.service';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
describe('NavigationService', () => {
	let service: NavigationService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(NavigationService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});