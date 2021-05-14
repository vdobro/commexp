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

import {Component, OnInit} from '@angular/core';
import {UserGroupService} from "@app/service/user-group.service";
import {NameCollisionError} from "@app/util/SessionUtils";
import {NavigationService} from "@app/service/navigation.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-create-group',
	templateUrl: './create-group.component.html',
	styleUrls: ['./create-group.component.scss']
})
export class CreateGroupComponent implements OnInit {

	model = {
		name: ''
	};

	loading = false;
	nameError = false;

	constructor(private readonly groupService: UserGroupService,
	            private readonly navigationService: NavigationService) {
	}

	ngOnInit(): void {
	}

	async submit() {
		this.loading = true;
		try {
			const group = await this.groupService.create(this.model.name);
			await this.navigationService.editGroup(group.id);
		} catch (e) {
			if (e instanceof NameCollisionError) {
				this.nameError = true;
			}
		}
		this.loading = false;
	}

	async goBack() {
		await this.navigationService.goToGroups();
	}
}
