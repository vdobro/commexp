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

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "@app/home/home.component";
import {ROOT_GROUPS, ROOT_USER} from "@app/util/UrlConfig";
import {NotFoundComponent} from "@app/not-found/not-found.component";

const routes: Routes = [
	{path: ROOT_GROUPS, loadChildren: () => import('./groups/groups.module').then(m => m.GroupsModule)},
	{path: ROOT_USER, loadChildren: () => import('./user/user.module').then(m => m.UserModule)},
	{path: '', component: HomeComponent, pathMatch: 'full'},
	{path: '**', component: NotFoundComponent}
];

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@NgModule({
	imports: [RouterModule.forRoot(routes, {useHash: true})],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
