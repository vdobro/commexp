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

import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {UserGroup} from "@app/model/user-group";
import {UserGroupService} from "@app/service/user-group.service";
import {SessionService} from "@app/service/state/session.service";
import {isUser} from "@app/util/SessionUtils";

type Group = UserGroup | null;

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class GroupSessionService {

	private readonly _activeGroup = new BehaviorSubject<Group>(null);
	private readonly _myGroups = new BehaviorSubject<UserGroup[]>([]);

	readonly activeGroup$ = this._activeGroup.asObservable();
	readonly myGroups$ = this._myGroups.asObservable();

	constructor(private readonly groupService: UserGroupService,
	            private readonly sessionService: SessionService,) {
		this.sessionService.session$.subscribe(async session => {
			if (isUser(session)) {
				await this.update();
			} else {
				this.setGroups([]);
				this.setActive(null);
			}
		});
		this.groupService.$groupsChanged.subscribe(async _ => {
			await this.update();
		});
	}

	async selectGroup(group: UserGroup) {
		this.setActive(group);
		await this.rememberLast(group);
	}

	async update() {
		const groups: UserGroup[] = await this.groupService.getMyGroups();
		this.setGroups(groups);

		await this.restoreLast(groups);
	}

	private async rememberLast(group: Group) {
		if (group) {
			await this.groupService.setDefault(group);
		}
	}

	private async restoreLast(groups: UserGroup[]) {
		const group = await this.groupService.getDefault();
		if (group) {
			this.setActive(group);
		} else if (groups.length > 0) {
			await this.selectGroup(groups[0]);
		}
	}

	private setGroups(groups: UserGroup[]) {
		this._myGroups.next(groups);
	}

	private setActive(group: Group) {
		this._activeGroup.next(group);
	}
}
