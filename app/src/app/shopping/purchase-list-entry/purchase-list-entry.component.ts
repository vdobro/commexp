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

import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';

import {User} from "@app/model/user";
import {Purchase} from "@app/model/purchase";
import {ShopService} from "@app/service/shop.service";
import {Shop} from "@app/model/shop";
import {UserGroupService} from "@app/service/user-group.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.20
 */
@Component({
	selector: 'app-purchase-list-entry',
	templateUrl: './purchase-list-entry.component.html',
	styleUrls: ['./purchase-list-entry.component.scss'],
	encapsulation: ViewEncapsulation.None,
})
export class PurchaseListEntryComponent implements OnInit {

	@Input()
	entry: Purchase | null = null;

	shop: Shop | null = null;
	doneBy: User | null = null;

	constructor(private readonly shopService: ShopService,
	            private readonly userService: UserGroupService) {
	}

	async ngOnInit() {
		if (this.entry) {
			this.shop = await this.shopService.getShop(this.entry.shopId);
			this.doneBy = await this.userService.getUser(this.entry.doneBy);
		}
	}
}
