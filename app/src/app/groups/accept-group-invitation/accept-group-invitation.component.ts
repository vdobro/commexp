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
import {SessionService} from '@app/service/state/session.service';
import {isUser} from '@app/util/SessionUtils';
import {UserGroupService} from '@app/service/user-group.service';
import {NavigationService} from '@app/service/navigation.service';
import {INVITATION_CODE_PARAM} from '@app/groups/links';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-accept-group-invitation',
	templateUrl: './accept-group-invitation.component.html',
	styleUrls: ['./accept-group-invitation.component.scss']
})
export class AcceptGroupInvitationComponent implements OnInit {

	model = {
		invitationId: ''
	};

	loggedIn = false;
	tokenError = false;
	joinSuccess = false;
	groupName = '';

	constructor(private readonly route: ActivatedRoute,
				private readonly session: SessionService,
				private readonly groupService: UserGroupService,
				private readonly navigation: NavigationService) {
		this.session.session$.subscribe(async current => {
			this.loggedIn = isUser(current);
			await this.trySubmit();
		});
		this.route.queryParams.subscribe(async params => {
			const token = params[INVITATION_CODE_PARAM];
			if (token) {
				this.model.invitationId = token;
				await this.trySubmit();
			}
		});
	}

	ngOnInit(): void {
	}

	private async trySubmit(): Promise<void> {
		if (this.loggedIn && this.model.invitationId) {
			await this.submit();
		}
	}

	async submit(): Promise<void> {
		try {
			const group = await this.groupService.joinGroup(this.model.invitationId);
			this.groupName = group.name;
			this.joinSuccess = true;
		} catch (e) {
			this.tokenError = true;
		}
	}

	async quit(): Promise<void> {
		await this.navigation.goToGroups();
	}

	resetError(): void {
		this.tokenError = false;
	}
}
