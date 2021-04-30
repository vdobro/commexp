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

import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {SessionService} from "@app/service/state/session.service";
import {isUser} from "@app/util/SessionUtils";
import {UserGroup} from "@app/model/user-group";
import {Session} from "@app/model/user-session";
import {MenuItem} from "primeng/api";
import {HeaderService} from "@app/service/state/header.service";
import {UserGroupService} from "@app/service/user-group.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HeaderComponent implements OnInit {

	menuItems: MenuItem[] = [];

	userLoggedIn = false;
	name = '';

	constructor(private readonly sessionService: SessionService,
	            private readonly headerService: HeaderService,
	            private readonly groupService: UserGroupService,
	            private readonly cdr: ChangeDetectorRef,
	) {
		this.menuItems = this.headerService.items;
		this.sessionService.session$.subscribe((session: Session) => {
			if (isUser(session)) {
				this.name = session.name;
				this.userLoggedIn = true;
			} else {
				this.name = '';
				this.userLoggedIn = false;
			}
			this.cdr.markForCheck();
		});
	}

	ngOnInit(): void {
	}

	async changeActiveGroup(group: UserGroup) {
		await this.groupService.setDefault(group);
	}
}
