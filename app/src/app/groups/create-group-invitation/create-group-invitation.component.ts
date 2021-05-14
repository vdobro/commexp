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

import {Component, Inject, OnInit} from '@angular/core';
import {Clipboard} from '@angular/cdk/clipboard';
import {DOCUMENT} from '@angular/common';
import {HttpParams} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

import {UserGroupService} from '@app/service/user-group.service';
import {GROUP_ID_PARAM, INVITATION_CODE_PARAM} from '@app/groups/links';
import {UserGroup} from '@app/model/user-group';
import {NavigationService} from "@app/service/navigation.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-create-group-invitation',
	templateUrl: './create-group-invitation.component.html',
	styleUrls: ['./create-group-invitation.component.scss']
})
export class CreateGroupInvitationComponent implements OnInit {

	group: UserGroup | null = null;

	invitationLink: string | null = null;
	loadingCode = false;
	linkCopied = false;

	private readonly baseUrl: string;

	constructor(private readonly route: ActivatedRoute,
				@Inject(DOCUMENT) document: any,
				private readonly groupService: UserGroupService,
				private readonly clipboard: Clipboard,
	            private readonly navigation: NavigationService) {
		this.baseUrl = document.location.origin + '/#/groups/join?';
	}

	ngOnInit(): void {
		this.route.params.subscribe(async params => {
			await this.loadGroup(params[GROUP_ID_PARAM]);
		});
	}

	async requestInvitation(): Promise<void> {
		if (!this.group) {
			return;
		}
		this.loadingCode = true;
		const result = await this.groupService.inviteUser(this.group);
		this.buildLink(result.code);
		this.loadingCode = false;
	}

	copyCode(): void {
		if (this.invitationLink != null) {
			this.clipboard.copy(this.invitationLink);
			this.linkCopied = true;
		}
	}

	async goBack() {
		await this.navigation.goBack();
	}

	private async loadGroup(groupId: string): Promise<void> {
		this.group = await this.groupService.get(groupId);
	}

	private buildLink(key: string): void {
		const params = new HttpParams().append(INVITATION_CODE_PARAM, key);
		this.invitationLink = this.baseUrl + params.toString();
	}
}
