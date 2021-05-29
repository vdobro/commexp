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
import {HttpClient} from '@angular/common/http';
import {environment} from '@environments/environment';
import {UserGroup} from '@app/model/user-group';
import {User} from '@app/model/user';
import {Observable, Subject} from 'rxjs';
import {ROOT_USER} from "@app/util/UrlConfig";

const GROUP_ROOT = environment.apiUrl + '/group';
const GROUP_LIST = GROUP_ROOT;
const GROUP_CREATE = GROUP_ROOT;
const GROUP_EDIT = GROUP_ROOT;
const GROUP_INVITE_USER = GROUP_ROOT + '/invite';
const GROUP_JOIN = GROUP_ROOT + '/join';
const GROUP_DEFAULT = GROUP_ROOT + '/default';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.04.10
 */
@Injectable({
	providedIn: 'root'
})
export class UserGroupService {

	private _groups : UserGroup[] = [];

	private readonly _groupsChanged = new Subject<any>();

	readonly $groupsChanged: Observable<any> = this._groupsChanged.asObservable();

	constructor(private readonly httpClient: HttpClient) {
		this._groupsChanged.subscribe(async _ => {
			await this.getMyGroups();
		});
	}

	async getMyGroups(): Promise<UserGroup[]> {
		this._groups = await this.httpClient.get<UserGroup[]>(GROUP_LIST).toPromise();
		return this._groups;
	}

	async create(name: string): Promise<UserGroup> {
		const result = await this.httpClient.post<UserGroup>(GROUP_CREATE, {
			name
		}).toPromise();
		this._groupsChanged.next();
		return result;
	}

	async get(id: string): Promise<UserGroup> {
		return await this.httpClient.get<UserGroup>(`${GROUP_ROOT}/${id}`).toPromise();
	}

	async inviteUser(group: UserGroup): Promise<InvitationDetails> {
		return await this.httpClient.post<InvitationDetails>(GROUP_INVITE_USER, {
			groupId: group.id,
		}).toPromise();
	}

	async getUsers(group: UserGroup): Promise<User[]> {
		return await this.httpClient.get<User[]>(`${GROUP_ROOT}/${group.id}/users`).toPromise();
	}

	async joinGroup(invitation: string): Promise<UserGroup> {
		const result = await this.httpClient.post<UserGroup>(`${GROUP_JOIN}/${invitation}`, {}).toPromise();
		this._groupsChanged.next();
		return result;
	}

	async getDefault(): Promise<UserGroup | null> {
		return await this.httpClient.get<UserGroup | null>(GROUP_DEFAULT).toPromise();
	}

	async setDefault(group: UserGroup): Promise<void> {
		const id = group.id;
		await this.httpClient.put(`${GROUP_DEFAULT}/${id}`, {}).toPromise();
	}

	async renameGroup(group: UserGroup, name: string): Promise<void> {
		const id = group.id;
		await this.httpClient.put(`${GROUP_EDIT}/${id}`, {
			name
		}).toPromise();
		this._groupsChanged.next();
	}

	async removeUser(group: UserGroup, user: User) : Promise<void> {
		const id = group.id;
		await this.httpClient.delete(`${GROUP_ROOT}/${id}/users/${user.username}`).toPromise();
	}

	async leaveGroup(group: UserGroup) : Promise<void> {
		const id = group.id;
		await this.httpClient.post(`${GROUP_ROOT}/${id}/leave`, {}).toPromise();
		this._groupsChanged.next();
	}

	async getUser(userId: string): Promise<User> {
		return await this.httpClient.get<User>(`${environment.apiUrl}/user/${userId}`).toPromise();
	}
}

export interface InvitationDetails {
	code: string;
	groupName: string;
}
