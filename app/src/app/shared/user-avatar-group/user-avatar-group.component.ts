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

import {Component, Input, OnInit} from '@angular/core';
import {User} from "@app/model/user";
import {generateColor} from "@app/util/AvatarUtils";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.04.28
 */
@Component({
	selector: 'app-user-avatar-group',
	templateUrl: './user-avatar-group.component.html',
	styleUrls: ['./user-avatar-group.component.scss']
})
export class UserAvatarGroupComponent implements OnInit {

	@Input()
	users: User[] = [];

	constructor() {
	}

	ngOnInit(): void {
	}

	getColor(user: User): string {
		return generateColor(user, 30, 50);
	}
}
