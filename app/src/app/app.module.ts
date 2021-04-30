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

import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {DropdownModule} from "primeng/dropdown";
import {HeaderComponent} from './header/header.component';
import {MenubarModule} from "primeng/menubar";
import {HomeComponent} from './home/home.component';
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
import {InputTextModule} from "primeng/inputtext";
import {SharedModule} from "primeng/api";
import {HttpErrorInterceptor} from "@app/util/HttpErrorInterceptor";
import {XsrfInterceptor} from "@app/util/XsrfInterceptor";
import {FormsModule} from "@angular/forms";
import {GroupSwitcherComponent} from './group-switcher/group-switcher.component';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@NgModule({
	declarations: [
		AppComponent,
		HeaderComponent,
		HomeComponent,
		GroupSwitcherComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		DropdownModule,
		HttpClientModule,
		HttpClientXsrfModule,
		AppRoutingModule,
		MenubarModule,
		InputTextModule,
		SharedModule,
		FormsModule
	],
	providers: [
		{provide: LocationStrategy, useClass: HashLocationStrategy},
		{provide: HTTP_INTERCEPTORS, useClass: XsrfInterceptor, multi: true},
		{provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true},
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
