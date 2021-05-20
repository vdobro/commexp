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
import {PurchaseService} from "@app/service/purchase.service";
import {Purchase} from "@app/model/purchase";
import {GroupSessionService} from "@app/service/state/group.service";
import {UserGroup} from "@app/model/user-group";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.20
 */
@Component({
	selector: 'app-purchases-list',
	templateUrl: './purchase-list.component.html',
	styleUrls: ['./purchase-list.component.scss']
})
export class PurchaseListComponent implements OnInit {

	purchases: Purchase[] = [];
	loading = true;
	group: UserGroup | null = null;

	constructor(private readonly purchaseService: PurchaseService,
	            private readonly groupSessionService: GroupSessionService,
	) {
		this.groupSessionService.activeGroup$.subscribe(async group => {
			this.group = group;
			await this.loadPurchases();
		})
	}

	async ngOnInit() {
		await this.loadPurchases();
	}

	private async loadPurchases() {
		if (this.group) {
			this.loading = true;
			this.purchases = await this.purchaseService.getAll(this.group);
			this.loading = false;
		}
	}
}
