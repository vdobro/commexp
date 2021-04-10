/*
 * Copyright (C) 2020 Vitalijus Dobrovolskis
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
import {HeaderService} from "@app/service/state/header.service";
import {SessionService} from "@app/service/state/session.service";
import {isUser} from "@app/util/SessionUtils";
import {UserGroup} from "@app/model/user-group";
import {Session} from "@app/model/user-session";
import {GroupSessionService} from "@app/service/state/group.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

	userLoggedIn = false;
	name = '';

	groups: UserGroup[] = [];
	activeGroup: UserGroup | null = null;

	constructor(public readonly headerService: HeaderService,
	            public readonly sessionService: SessionService,
	            private readonly groupSessionService: GroupSessionService,
	) {
		this.sessionService.session$.subscribe((session: Session) => {
			if (isUser(session)) {
				this.name = session.name;
				this.userLoggedIn = true;
			} else {
				this.userLoggedIn = false;
				this.name = '';
			}
		});
		this.groupSessionService.myGroups$.subscribe(groups => {
			this.groups = groups;
		})
		this.groupSessionService.activeGroup$.subscribe((group) => {
			this.activeGroup = group;
		})
	}

	ngOnInit(): void {
	}

	async onChangeSelection() {
		if (this.activeGroup) {
			await this.groupSessionService.selectGroup(this.activeGroup);
		}
	}
}
