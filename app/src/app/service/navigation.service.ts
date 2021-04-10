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

import {Injectable} from '@angular/core';
import {Router} from "@angular/router";
import {UserHeaderService} from "@app/service/state/user-header.service";
import {SessionService} from "@app/service/state/session.service";
import {isUser} from "@app/util/SessionUtils";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class NavigationService {

	constructor(private readonly router: Router,
	            private readonly sessionService: SessionService,
	            private readonly userHeaderService: UserHeaderService) {
		sessionService.session$.subscribe(async (session) => {
			if (isUser(session)) {
				await this.navigateToLast();
			}
		})
	}

	async home() {
		await this.router.navigate(['']);
	}

	private async navigateToLast() {
		const nav = this.router.getCurrentNavigation();
		if (!nav || !nav.previousNavigation) {
			await this.home();
		} else {
			await this.router.navigateByUrl(nav.previousNavigation.finalUrl || '/');
		}
	}
}
