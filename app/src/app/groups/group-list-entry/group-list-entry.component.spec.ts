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

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GroupListEntryComponent} from './group-list-entry.component';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.13
 */
describe('GroupListEntryComponent', () => {
	let component: GroupListEntryComponent;
	let fixture: ComponentFixture<GroupListEntryComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [GroupListEntryComponent]
		}).compileComponents();
	});

	beforeEach(() => {
		fixture = TestBed.createComponent(GroupListEntryComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
