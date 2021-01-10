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

import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {MenubarModule} from "primeng/menubar";
import {HomeComponent} from './home/home.component';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@NgModule({
	declarations: [
		AppComponent,
		HeaderComponent,
		HomeComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		MenubarModule
	],
	providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
	bootstrap: [AppComponent]
})
export class AppModule {
}
