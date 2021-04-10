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
import {HttpClient} from "@angular/common/http";
import {environment} from "@environments/environment";
import {UserGroup} from "@app/model/user-group";
import {User} from "@app/model/user";

const GROUP_ROOT = environment.apiUrl + "/group";
const GROUP_LIST = GROUP_ROOT;
const GROUP_CREATE = GROUP_ROOT;
const GROUP_INVITE_USER = GROUP_ROOT + "/invite";
const GROUP_JOIN = GROUP_ROOT + "/join";
const GROUP_DEFAULT = GROUP_ROOT + "/default";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.04.10
 */
@Injectable({
	providedIn: 'root'
})
export class UserGroupService {

	constructor(private readonly httpClient: HttpClient,) {
	}

	async getMyGroups(): Promise<UserGroup[]> {
		return await this.httpClient.get<UserGroup[]>(GROUP_LIST).toPromise();
	}

	async create(name: string): Promise<UserGroup> {
		return await this.httpClient.post<UserGroup>(GROUP_CREATE, {
			name: name
		}).toPromise();
	}

	async get(id: string): Promise<UserGroup> {
		return await this.httpClient.get<UserGroup>(`${GROUP_ROOT}/${id}`).toPromise();
	}

	async inviteUser(group: UserGroup, user: User): Promise<InvitationDetails> {
		return await this.httpClient.post<InvitationDetails>(GROUP_INVITE_USER, {
			username: user.username,
			groupId: group.id,
		}).toPromise();
	}

	async acceptInvitation(invitation: string): Promise<UserGroup> {
		return await this.httpClient.post<UserGroup>(`${GROUP_JOIN}/${invitation}`, {}).toPromise();
	}

	async getDefault(): Promise<UserGroup | null> {
		return await this.httpClient.get<UserGroup | null>(GROUP_DEFAULT).toPromise();
	}

	async setDefault(group: UserGroup) {
		const id = group.id;
		await this.httpClient.put(`${GROUP_DEFAULT}/${id}`, {}).toPromise();
	}
}

export interface InvitationDetails {
	code: string,
	invitedUser: string,
	groupName: string,
}
