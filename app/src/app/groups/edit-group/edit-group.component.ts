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
import {ActivatedRoute} from '@angular/router';

import {ConfirmationService} from 'primeng/api';

import {UserGroup} from '@app/model/user-group';
import {UserGroupService} from '@app/service/user-group.service';
import {GROUP_ID_PARAM} from '@app/groups/links';
import {User} from '@app/model/user';
import {SessionService} from "@app/service/state/session.service";
import {isUser} from "@app/util/SessionUtils";
import {NavigationService} from "@app/service/navigation.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.10
 */
@Component({
	selector: 'app-edit-group',
	templateUrl: './edit-group.component.html',
	styleUrls: ['./edit-group.component.scss'],
	providers: [ConfirmationService]
})
export class EditGroupComponent implements OnInit {

	group: UserGroup | null = null;

	initializing = false;
	savingChanges = false;
	groupUsers: User[] = [];

	constructor(private readonly route: ActivatedRoute,
	            private readonly confirmationService: ConfirmationService,
	            private readonly groupService: UserGroupService,
	            private readonly navigationService: NavigationService,
	            private readonly sessionService: SessionService) {
	}

	ngOnInit(): void {
		this.route.params.subscribe(async params => {
			await this.loadGroup(params[GROUP_ID_PARAM]);
		});
	}

	private async loadGroup(groupId: string): Promise<void> {
		this.initializing = true;
		this.group = await this.groupService.get(groupId);
		this.groupUsers = await this.getUsersExceptCurrent(this.group);
		this.initializing = false;
	}

	async saveGroupChanges(): Promise<void> {
		if (this.group) {
			await this.groupService.renameGroup(this.group, this.group.name);
		}
	}

	async deleteUser(user: User): Promise<void> {
		if (this.group) {
			await this.groupService.removeUser(this.group, user);
			this.groupUsers = this.groupUsers.filter(x => x.id !== user.id);
		}
	}

	async leaveGroup(): Promise<void> {
		if (this.group) {
			const group = this.group!!;
			this.confirmationService.confirm({
				message: `Are you sure you want to leave the group ${group.name}? This action is irreversible.`,
				accept: async () => {
					await this.groupService.leaveGroup(group);
					await this.navigationService.goToGroups();
				}
			});
		}
	}

	private async getUsersExceptCurrent(group: UserGroup): Promise<User[]> {
		const user = this.sessionService.current;
		if (isUser(user)) {
			const allUsers = await this.groupService.getUsers(group);
			const currentId = user.id;
			return allUsers.filter((x) => x.id !== currentId);
		}
		return [];
	}
}
