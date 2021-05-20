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

import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {PanelModule} from "primeng/panel";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {CardModule} from "primeng/card";

import {SharedModule} from "@app/shared/shared.module";

import {PurchaseViewComponent} from "./purchase-view/purchase-view.component";
import {PurchaseListComponent} from './purchase-list/purchase-list.component';
import {ShopViewComponent} from "./shop-view/shop-view.component";
import {ShoppingRoutingModule} from "@app/shopping/shopping-routing.module";
import {PurchaseListEntryComponent} from './purchase-list-entry/purchase-list-entry.component';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.20
 */
@NgModule({
	declarations: [
		ShopViewComponent,
		PurchaseViewComponent,
		PurchaseListComponent,
		PurchaseListEntryComponent
	],
	imports: [
		CommonModule,
		SharedModule,
		ShoppingRoutingModule,
		PanelModule,
		CardModule,
		FormsModule,
		ButtonModule,
		InputTextModule
	]
})
export class ShoppingModule {
}
