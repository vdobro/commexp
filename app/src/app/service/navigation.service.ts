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
import {NavigationEnd, Router} from '@angular/router';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Injectable({
	providedIn: 'root'
})
export class NavigationService {

	private lastLocation : string | null = null;
	private currentLocation : string | null = null;

	constructor(private readonly router: Router) {
		this.router.events.subscribe((event) => {
			if (event instanceof NavigationEnd) {
				this.lastLocation = this.currentLocation;
				this.currentLocation = event.urlAfterRedirects;
			}
		});
	}

	async home(): Promise<void> {
		await this.router.navigate(['']);
	}

	async editGroup(id: string): Promise<void> {
		await this.router.navigate(['groups', id, 'edit']);
	}

	async goToGroups(): Promise<void> {
		await this.router.navigate(['groups']);
	}

	async goToShopping(): Promise<void> {
		await this.router.navigate(['shopping']);
	}

	async goBack() : Promise<void> {
		if (this.lastLocation) {
			await this.router.navigateByUrl(this.lastLocation);
			this.currentLocation = this.lastLocation;
			this.lastLocation = null;
		}
	}
}
