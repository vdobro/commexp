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

import {UserGroup} from "@app/model/user-group";
import {User} from "@app/model/user";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-groups',
	templateUrl: './group-list.component.html',
	styleUrls: ['./group-list.component.scss']
})
export class GroupListComponent implements OnInit {

	groups: GroupWithUsers[] = [];

	constructor(private readonly groupService: UserGroupService) {
	}

	async ngOnInit() {
		const groups = await this.groupService.getMyGroups();
		this.groups = await Promise.all(groups.map(group => this.mapToGroupWithUsers(group)));
	}

	async getUsers(group: UserGroup): Promise<User[]> {
		return await this.groupService.getUsers(group);
	}

	async mapToGroupWithUsers(group: UserGroup): Promise<GroupWithUsers> {
		return {
			id: group.id,
			name: group.name,
			users: await this.getUsers(group),
		};
	}
}

interface GroupWithUsers {
	id: string,
	name: string,
	users: User[]
}
