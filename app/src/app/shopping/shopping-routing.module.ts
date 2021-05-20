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
import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {PurchaseListComponent} from "@app/shopping/purchase-list/purchase-list.component";
import {links, paramLinks} from "@app/shopping/links";
import {PurchaseViewComponent} from "@app/shopping/purchase-view/purchase-view.component";
import {ShopViewComponent} from "@app/shopping/shop-view/shop-view.component";

const routes: Routes = [
	{path: '', component: PurchaseListComponent},
	{path: links.lists, component: PurchaseListComponent},
	{path: paramLinks.list, component: PurchaseViewComponent},
	{path: paramLinks.shop, component: ShopViewComponent},
];

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.20
 */
@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class ShoppingRoutingModule {
}
