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
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";

import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {MessageModule} from 'primeng/message';
import {PasswordModule} from 'primeng/password';

import {UserRoutingModule} from './user-routing.module';
import {UserComponent} from './user.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@NgModule({
	declarations: [UserComponent, LoginComponent, RegisterComponent],
	imports: [
		CommonModule,
		UserRoutingModule,
		FormsModule,
		ButtonModule,
		PasswordModule,
		InputTextModule,
		MessageModule
	],
	providers: [InputTextModule, ButtonModule]
})
export class UserModule {
}
