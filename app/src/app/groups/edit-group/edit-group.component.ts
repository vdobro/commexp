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
import {ActivatedRoute} from "@angular/router";

import {UserGroup} from "@app/model/user-group";
import {UserGroupService} from "@app/service/user-group.service";
import {GROUP_ID_PARAM} from "@app/groups/links";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.10
 */
@Component({
	selector: 'app-edit-group',
	templateUrl: './edit-group.component.html',
	styleUrls: ['./edit-group.component.scss']
})
export class EditGroupComponent implements OnInit {

	group: UserGroup | null = null;

	initializing = false;

	constructor(private readonly route: ActivatedRoute,
	            private readonly groupService: UserGroupService) {
	}

	ngOnInit(): void {
		this.route.params.subscribe(async params => {
			await this.loadGroup(params[GROUP_ID_PARAM]);
		});
	}

	private async loadGroup(groupId: string) {
		this.initializing = true;
		this.group = await this.groupService.get(groupId);
		this.initializing = false;
	}
}
