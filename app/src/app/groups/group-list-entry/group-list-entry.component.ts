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

import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';

import {NavigationService} from "@app/service/navigation.service";
import {UserGroup} from "@app/model/user-group";
import {User} from "@app/model/user";
import {UserGroupService} from "@app/service/user-group.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-group-card',
	templateUrl: './group-list-entry.component.html',
	styleUrls: ['./group-list-entry.component.scss'],
	encapsulation: ViewEncapsulation.None,
})
export class GroupListEntryComponent implements OnInit {

	@Input()
	group: UserGroup | null = null;

	loading = true;
	users: User[] = [];

	constructor(private readonly navService: NavigationService,
	            private readonly groupService: UserGroupService) {
	}

	async ngOnInit() {
		await this.loadUsers();

		this.loading = false;
	}

	async editGroup(group: UserGroup) {
		await this.navService.editGroup(group.id);
	}

	async loadUsers() {
		if (this.group) {
			this.users = await this.groupService.getUsers(this.group);
		}
	}
}
