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

import {ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, OnInit, Output} from '@angular/core';

import {UserGroup} from "@app/model/user-group";
import {GroupSessionService} from "@app/service/state/group.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.04.29
 */
@Component({
	selector: 'app-group-switcher',
	templateUrl: './group-switcher.component.html',
	styleUrls: ['./group-switcher.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GroupSwitcherComponent implements OnInit {

	groups: UserGroup[] = [];
	activeGroup: UserGroup | null = null;

	@Output()
	groupSelect = new EventEmitter<UserGroup>();

	constructor(private readonly groupService: GroupSessionService,
	            private readonly cdr: ChangeDetectorRef,
	) {
		this.groupService.myGroups$.subscribe(groups => {
			this.groups = groups;
			this.cdr.markForCheck();
		});
		this.groupService.activeGroup$.subscribe(active => {
			this.activeGroup = active;
			this.cdr.markForCheck();
		});
	}

	async ngOnInit() {
	}

	async onChangeSelection() {
		if (this.activeGroup) {
			await this.groupSelect.emit(this.activeGroup);
		}
	}
}

