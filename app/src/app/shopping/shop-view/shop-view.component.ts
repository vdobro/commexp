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
import {Shop} from "@app/model/shop";
import {GROUP_ID_PARAM} from "@app/groups/links";
import {ActivatedRoute} from "@angular/router";
import {ShopService} from "@app/service/shop.service";

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.20
 */
@Component({
	selector: 'app-shop-view',
	templateUrl: './shop-view.component.html',
	styleUrls: ['./shop-view.component.scss']
})
export class ShopViewComponent implements OnInit {

	shop: Shop | null = null;

	shopName = '';
	initializing = true;
	savingChanges = false;

	constructor(private readonly route: ActivatedRoute,
	            private readonly shopService: ShopService) {
	}

	ngOnInit(): void {
		this.route.params.subscribe(async params => {
			this.initializing = true;
			await this.loadShop(params[GROUP_ID_PARAM]);
			this.initializing = false;
		});
	}

	private async loadShop(shopId: string) {
		this.shop = await this.shopService.getShop(shopId);
	}

	async saveShopChanges() {
		//TODO
	}
}
